package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SectionStatusEnum;
import com.jam2.bowebmanagementservice.constant.SystemConstants;
import com.jam2.bowebmanagementservice.entity.SubPortfolioContent;
import com.jam2.bowebmanagementservice.entity.WebPortfolioContent;
import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.repository.SubPortfolioContentRepository;
import com.jam2.bowebmanagementservice.repository.WebPortfolioContentRepository;
import com.jam2.bowebmanagementservice.util.DateTimeUtil;
import com.jam2.bowebmanagementservice.util.StringCheckUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class WebPortfolioContentService {

    private final WebPortfolioContentRepository webPortfolioContentRepository;
    private final SubPortfolioContentRepository subPortfolioContentRepository;
    private final DateTimeUtil dateTimeUtil;
    private final StringCheckUtil stringCheckUtil;

    public WebPortfolioContentService(WebPortfolioContentRepository webPortfolioContentRepository, SubPortfolioContentRepository subPortfolioContentRepository,
                                      DateTimeUtil dateTimeUtil, StringCheckUtil stringCheckUtil) {
        this.webPortfolioContentRepository = webPortfolioContentRepository;
        this.subPortfolioContentRepository = subPortfolioContentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.stringCheckUtil= stringCheckUtil;
    }

    public WebIdResponse getWebPortfolioId(String userWebId){
        WebIdResponse resp = new WebIdResponse();
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByUserWebId(UUID.fromString(userWebId));

        if(null != webPortfolioContent){
            resp.setWebId(webPortfolioContent.getWebPortfolioId().toString());
        }

        return resp;
    }

    public WebPortfolioContentResponse getWebPortfolioContentDetail(String webPortfolioId){
        WebPortfolioContentResponse resp = new WebPortfolioContentResponse();
        List<SubPortfolioContentResponse> subRespList =new ArrayList<>();
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(UUID.fromString(webPortfolioId));
        List<SubPortfolioContent> subPortfolioContents =
                subPortfolioContentRepository.findBySectionStatusAndWebPortfolioContentWebPortfolioId(SectionStatusEnum.ACTIVE.getStatusCode(),UUID.fromString(webPortfolioId));

        if(null != subPortfolioContents){
            for(SubPortfolioContent subPortfolioContent : subPortfolioContents){
                SubPortfolioContentResponse subResp = new SubPortfolioContentResponse();
                BeanUtils.copyProperties(subPortfolioContent,subResp);

                subRespList.add(subResp);
            }
        }

        if(null != webPortfolioContent){
            resp.setWebPortfolioId(webPortfolioContent.getWebPortfolioId());
            resp.setDescription(webPortfolioContent.getDescription());
            resp.setImageUrl(webPortfolioContent.getImageUrl());
            resp.setTitle(webPortfolioContent.getTitle());
            resp.setSubPortfolioContents(subRespList);
        }

        return resp;
    }


    public WebPortfolioContentCreateResponse updateWebPortfolioContentDetail(WebPortfolioContentCreateRequest request){
        List<SubPortfolioContentCreateResponse> subRespList =new ArrayList<>();
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(UUID.fromString(request.getWebPortfolioId()));

        if(null == webPortfolioContent){
           // throw
        }

        List<SubPortfolioContent> subPortfolioContents = createSubPortfolioList(request);

        webPortfolioContent.setTitle(stringCheckUtil.isStringNEB(request.getTitle()) ? webPortfolioContent.getTitle() : request.getTitle());
        webPortfolioContent.setDescription(stringCheckUtil.isStringNEB(request.getDescription()) ? webPortfolioContent.getDescription() : request.getDescription());
        webPortfolioContent.setImageUrl(stringCheckUtil.isStringNEB(request.getImageUrl()) ? webPortfolioContent.getImageUrl() : request.getImageUrl());
        webPortfolioContent.setSubPortfolioContent(subPortfolioContents);
        webPortfolioContent.setUpdatedBy("user"); // add token user
        webPortfolioContent.setUpdatedDate(dateTimeUtil.now());
        webPortfolioContentRepository.save(webPortfolioContent);

        if(null != subPortfolioContents){
            for(SubPortfolioContent subPortfolioContent : subPortfolioContents){
                SubPortfolioContentCreateResponse subResp = new SubPortfolioContentCreateResponse();
                BeanUtils.copyProperties(subPortfolioContent,subResp);

                subRespList.add(subResp);
            }
        }

        return WebPortfolioContentCreateResponse.builder()
                .description(webPortfolioContent.getDescription())
                .title(webPortfolioContent.getTitle())
                .imageUrl(webPortfolioContent.getImageUrl())
                .subPortfolioContents(subRespList)
                .build();
    }

    public SubPortfolioContentResponse updateSubPortfolioContentDetail(SubPortfolioContentRequest req){
        SubPortfolioContentResponse resp = new SubPortfolioContentResponse();

        SubPortfolioContent subPortfolioContent = subPortfolioContentRepository.findBySubPortfolioId(UUID.fromString(req.getSubPortfolioId()));

        if (null == subPortfolioContent){
            // throw
        }

        subPortfolioContent.setDescription(stringCheckUtil.isStringNEB(req.getDescription())? subPortfolioContent.getDescription() : req.getDescription());
        subPortfolioContent.setTitle(stringCheckUtil.isStringNEB(req.getTitle())? subPortfolioContent.getTitle() : req.getTitle());
        subPortfolioContent.setImageUrl(stringCheckUtil.isStringNEB(req.getImageUrl())? subPortfolioContent.getImageUrl() : req.getImageUrl());
        subPortfolioContent.setUpdatedBy("user");
        subPortfolioContent.setUpdatedDate(dateTimeUtil.now());
        subPortfolioContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
        subPortfolioContentRepository.save(subPortfolioContent);

        BeanUtils.copyProperties(subPortfolioContent,resp);

        return resp;
    }

    public void deleteSubPortfolioContentDetail(String subPortfolioId){

        SubPortfolioContent subPortfolioContent = subPortfolioContentRepository.findBySubPortfolioId(UUID.fromString(subPortfolioId));

        if (null == subPortfolioContent){
            // unable to delete
        }

        subPortfolioContent.setSectionStatus(SectionStatusEnum.INACTIVE.getStatusCode());
        subPortfolioContentRepository.save(subPortfolioContent);

    }

    private List<SubPortfolioContent> createSubPortfolioList(WebPortfolioContentCreateRequest request){
        List<SubPortfolioContent> subPortfolioContentList = new ArrayList<>();


        if(null == request.getSubPortfolioContents() ||
                request.getSubPortfolioContents().isEmpty()){
            return subPortfolioContentList;
        }

        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(UUID.fromString(request.getWebPortfolioId()));

        request.getSubPortfolioContents().forEach( s -> {
                    SubPortfolioContent subPortfolioContent = new SubPortfolioContent();
                    subPortfolioContent.setDescription(s.getDescription());
                    subPortfolioContent.setImageUrl(s.getImageUrl());
                    subPortfolioContent.setTitle(s.getTitle());
                    subPortfolioContent.setCreatedBy(SystemConstants.SYSTEM);
                    subPortfolioContent.setCreatedDate(dateTimeUtil.now());
                    subPortfolioContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
                    subPortfolioContent.setWebPortfolioContent(webPortfolioContent);

                    subPortfolioContentRepository.save(subPortfolioContent);
                });

        subPortfolioContentList = subPortfolioContentRepository.findByWebPortfolioContentWebPortfolioId(webPortfolioContent.getWebPortfolioId());

        return subPortfolioContentList;

    }



}
