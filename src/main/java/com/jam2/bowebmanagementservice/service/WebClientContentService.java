package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SectionStatusEnum;
import com.jam2.bowebmanagementservice.entity.*;
import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.repository.SubClientContentRepository;
import com.jam2.bowebmanagementservice.repository.WebClientContentRepository;
import com.jam2.bowebmanagementservice.repository.WebHeroContentRepository;
import com.jam2.bowebmanagementservice.util.DateTimeUtil;
import com.jam2.bowebmanagementservice.util.ImageUtil;
import com.jam2.bowebmanagementservice.util.RawStringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Service
public class WebClientContentService {

    private final WebClientContentRepository webClientContentRepository;
    private final SubClientContentRepository subClientContentRepository;
    private final DateTimeUtil dateTimeUtil;
    private final RawStringUtil stringCheckUtil;
    private final ImageUtil imageUtil;

    public WebClientContentService(WebClientContentRepository webClientContentRepository, SubClientContentRepository subClientContentRepository,
                                   DateTimeUtil dateTimeUtil, RawStringUtil stringCheckUtil, ImageUtil imageUtil) {
        this.webClientContentRepository = webClientContentRepository;
        this.subClientContentRepository = subClientContentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.stringCheckUtil = stringCheckUtil;
        this.imageUtil = imageUtil;
    }


    public WebIdResponse getWebClientId(String userWebId){
        WebIdResponse resp = new WebIdResponse();
        WebClientContent webClientContent = webClientContentRepository.findByUserWebId(UUID.fromString(userWebId));

        if(null != webClientContent){
            resp.setWebId(webClientContent.getWebClientId().toString());
        }

        return resp;
    }

    public WebClientContentResponse getWebClientContentDetail(String webClientId){
        WebClientContentResponse resp = new WebClientContentResponse();
        WebClientContent webClientContent = webClientContentRepository.findByWebClientId(UUID.fromString(webClientId));

        List<SubClientContentResponse> subClientContentResponseList = new ArrayList<>();
        List<SubClientContent> subClientContents =
                subClientContentRepository.findBySectionStatusAndWebClientContentWebClientId(SectionStatusEnum.ACTIVE.getStatusCode(), UUID.fromString(webClientId));

        if(null != subClientContents){
            for(SubClientContent subClientContent : subClientContents){
                SubClientContentResponse subResp = new SubClientContentResponse();
                BeanUtils.copyProperties(subClientContent,subResp);

                subClientContentResponseList.add(subResp);
            }
        }

        if(null != webClientContent){
            resp.setWebClientId(webClientContent.getWebClientId());
            resp.setDescription(webClientContent.getDescription());
            resp.setImageUrl(webClientContent.getImageUrl());
            resp.setTitle(webClientContent.getTitle());
            resp.setSubClientContents(subClientContentResponseList);

        }

        return resp;
    }

    public WebClientContentResponse updateWebClientContentDetail(WebClientContentRequest webClientContentRequest){
        WebClientContentResponse resp = new WebClientContentResponse();
        if(null == webClientContentRequest){
//            throw
        }

        WebClientContent webClientContent = webClientContentRepository.findByWebClientId(UUID.fromString(webClientContentRequest.getWebClientId()));

        if(null == webClientContent){
            // throw
        }

        String imagePath = "";
        imagePath = webClientContent.getImageUrl();

        webClientContent.setTitle(stringCheckUtil.isStringNEB(webClientContentRequest.getTitle()) ? webClientContent.getTitle() : webClientContent.getTitle());
        webClientContent.setDescription(stringCheckUtil.isStringNEB(webClientContentRequest.getDescription()) ? webClientContent.getDescription(): webClientContent.getDescription());
        webClientContent.setImageUrl(imagePath);
        webClientContent.setUpdatedBy("user"); // add token user
        webClientContent.setUpdatedDate(dateTimeUtil.now());
        webClientContentRepository.save(webClientContent);

        BeanUtils.copyProperties(webClientContent,resp);

        return resp;
    }

    //sub client
    public HashMap<String,Object> createSubClientContent(SubClientCreateContentRequest request){

        List<SubClientContent> subClientContentList= subClientContentRepository.findBySectionStatusAndWebClientContentWebClientId(SectionStatusEnum.ACTIVE.getStatusCode(),UUID.fromString(request.getWebClientId()));
        WebClientContent webClientContent = webClientContentRepository.findByWebClientId(UUID.fromString(request.getWebClientId()));

        if(null != subClientContentList && subClientContentList.size() >= 6){
//                throw
        }

//        String imagePath = saveImage(request.getImageUrl(), webAboutContent.getImageUrl());

        SubClientContent subClientContent = SubClientContent.builder()
                .title(request.getTitle())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .customerName(request.getCustomerName())
                .createdDate(dateTimeUtil.now())
                .createdBy("User")
                .sectionStatus(SectionStatusEnum.ACTIVE.getStatusCode())
                .webClientContent(webClientContent)
                .build();

        subClientContentRepository.save(subClientContent);

        HashMap<String,Object> resp = new HashMap<>();

        resp.put("status", "update successfully");


        return resp;
    }

    //sub client
    public SubClientContentResponse updateSubClientContent(SubClientContentRequest request){
        SubClientContentResponse resp = new SubClientContentResponse();

        if(null == request.getSubClientId()){
            System.out.println(request);
        }

        SubClientContent subClientContent = subClientContentRepository.findBySubClientId(UUID.fromString(request.getSubClientId()));

        if (null == subClientContent){
            // throw
        }

//        String imagePath = saveImage(request.getImageUrl(), subAboutContent.getImageUrl());

        subClientContent.setDescription(stringCheckUtil.isStringNEB(request.getDescription())? subClientContent.getDescription() : request.getDescription());
        subClientContent.setTitle(stringCheckUtil.isStringNEB(request.getTitle())? subClientContent.getTitle() : request.getTitle());
        subClientContent.setImageUrl(stringCheckUtil.isStringNEB(request.getImageUrl())? subClientContent.getImageUrl() : request.getImageUrl());
        subClientContent.setUpdatedBy("user");
        subClientContent.setUpdatedDate(dateTimeUtil.now());
        subClientContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
        subClientContentRepository.save(subClientContent);

        BeanUtils.copyProperties(subClientContent,resp);

        return resp;
    }

    public void inactiveSubClientContentDetail(String subClientId){

        SubClientContent subClientContent = subClientContentRepository.findBySubClientId(UUID.fromString(subClientId));

        if (null == subClientContent){
            // unable to delete
        }

        subClientContent.setSectionStatus(SectionStatusEnum.INACTIVE.getStatusCode());
        subClientContentRepository.save(subClientContent);

    }

}
