package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SectionStatusEnum;
import com.jam2.bowebmanagementservice.entity.SubPortfolioContent;
import com.jam2.bowebmanagementservice.entity.WebPortfolioContent;
import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.repository.SubPortfolioContentRepository;
import com.jam2.bowebmanagementservice.repository.WebPortfolioContentRepository;
import com.jam2.bowebmanagementservice.util.DateTimeUtil;
import com.jam2.bowebmanagementservice.util.ImageUtil;
import com.jam2.bowebmanagementservice.util.RawStringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class WebPortfolioContentService {

    private final WebPortfolioContentRepository webPortfolioContentRepository;
    private final SubPortfolioContentRepository subPortfolioContentRepository;
    private final DateTimeUtil dateTimeUtil;
    private final RawStringUtil stringCheckUtil;
    private final ImageUtil imageUtil;

    public WebPortfolioContentService(WebPortfolioContentRepository webPortfolioContentRepository, SubPortfolioContentRepository subPortfolioContentRepository,
                                      DateTimeUtil dateTimeUtil, RawStringUtil stringCheckUtil, ImageUtil imageUtil) {
        this.webPortfolioContentRepository = webPortfolioContentRepository;
        this.subPortfolioContentRepository = subPortfolioContentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.stringCheckUtil= stringCheckUtil;
        this.imageUtil = imageUtil;
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


    /**
     *
     * create main portfolio
     *
     * @param request
     * @return
     */
    public  HashMap<String,Object> updateWebPortfolioContentDetail(WebPortfolioContentCreateRequest request){
        List<SubPortfolioContentCreateResponse> subRespList =new ArrayList<>();
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(UUID.fromString(request.getWebPortfolioId()));

        if(null == webPortfolioContent){
           // throw
        }

//        List<SubPortfolioContent> subPortfolioContents = subPortfolioContentRepository.findBySectionStatusAndWebPortfolioContentWebPortfolioId(SectionStatusEnum.ACTIVE.getStatusCode(),UUID.fromString(request.getWebPortfolioId()));

        String imagePath = saveImage(request.getImageUrl(), webPortfolioContent.getImageUrl());

        webPortfolioContent.setTitle(stringCheckUtil.isStringNEB(request.getTitle()) ? webPortfolioContent.getTitle() : request.getTitle());
        webPortfolioContent.setDescription(stringCheckUtil.isStringNEB(request.getDescription()) ? webPortfolioContent.getDescription() : request.getDescription());
        webPortfolioContent.setImageUrl(imagePath);
        webPortfolioContent.setUpdatedBy("user"); // add token user
        webPortfolioContent.setUpdatedDate(dateTimeUtil.now());
        webPortfolioContentRepository.save(webPortfolioContent);

//        if(null != subPortfolioContents){
//            for(SubPortfolioContent subPortfolioContent : subPortfolioContents){
//                SubPortfolioContentCreateResponse subResp = new SubPortfolioContentCreateResponse();
//                BeanUtils.copyProperties(subPortfolioContent,subResp);
//
//                subRespList.add(subResp);
//            }
//        }

        HashMap<String,Object> resp = new HashMap<>();

        resp.put("status", "update successfully");


        return resp;
    }


    public void deleteSubPortfolioContentDetail(String subPortfolioId){

        SubPortfolioContent subPortfolioContent = subPortfolioContentRepository.findBySubPortfolioId(UUID.fromString(subPortfolioId));

        if (null == subPortfolioContent){
            // unable to delete
        }

        subPortfolioContent.setSectionStatus(SectionStatusEnum.DELETE.getStatusCode());
        subPortfolioContentRepository.save(subPortfolioContent);

    }

    public void inactiveSubPortfolioContentDetail(String subPortfolioId){

        SubPortfolioContent subPortfolioContent = subPortfolioContentRepository.findBySubPortfolioId(UUID.fromString(subPortfolioId));

        if (null == subPortfolioContent){
            // unable to delete
        }

        subPortfolioContent.setSectionStatus(SectionStatusEnum.INACTIVE.getStatusCode());
        subPortfolioContentRepository.save(subPortfolioContent);

    }

    /**
     *
     * Max 6 sub
     * add positioning
     *
     * @param request
     * @return
     */
//    private List<SubPortfolioContent> createSubPortfolioList(WebPortfolioContentCreateRequest request){
//        List<SubPortfolioContent> subPortfolioContentList = new ArrayList<>();
//
//
//        if(null == request.getSubPortfolioContents() || request.getSubPortfolioContents().isEmpty()){
//            return subPortfolioContentList;
//        }
//
//        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(UUID.fromString(request.getWebPortfolioId()));
//
//        request.getSubPortfolioContents().forEach( s -> {
//                    SubPortfolioContent subPortfolioContent = new SubPortfolioContent();
//                    subPortfolioContent.setDescription(s.getDescription());
//                    subPortfolioContent.setImageUrl(s.getImageUrl());
//                    subPortfolioContent.setTitle(s.getTitle());
//                    subPortfolioContent.setCreatedBy(SystemConstants.SYSTEM);
//                    subPortfolioContent.setCreatedDate(dateTimeUtil.now());
//                    subPortfolioContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
//                    subPortfolioContent.setWebPortfolioContent(webPortfolioContent);
//
//                    subPortfolioContentRepository.save(subPortfolioContent);
//                });
//
//        subPortfolioContentList = subPortfolioContentRepository.findByWebPortfolioContentWebPortfolioId(webPortfolioContent.getWebPortfolioId());
//
//        return subPortfolioContentList;
//
//    }

    public  HashMap<String,Object> createSubPortfolioContent(SubPortfolioContentCreateRequest request){

        List<SubPortfolioContent> subPortfolioContentList= subPortfolioContentRepository.findBySectionStatusAndWebPortfolioContentWebPortfolioId(SectionStatusEnum.ACTIVE.getStatusCode(),UUID.fromString(request.getWebPortfolioId()));
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(UUID.fromString(request.getWebPortfolioId()));

        if(null != subPortfolioContentList && subPortfolioContentList.size() >= 6){
//                throw
        }

        String imagePath = saveImage(request.getImageUrl(), webPortfolioContent.getImageUrl());

        SubPortfolioContent subPortfolioContent = SubPortfolioContent.builder()
                .title(request.getTitle())
                .imageUrl(imagePath)
                .description(request.getDescription())
                .createdDate(dateTimeUtil.now())
                .createdBy("User")
                .sectionStatus(SectionStatusEnum.ACTIVE.getStatusCode())
                .webPortfolioContent(webPortfolioContent)
                .build();

        subPortfolioContentRepository.save(subPortfolioContent);

        HashMap<String,Object> resp = new HashMap<>();

        resp.put("status", "update successfully");


        return resp;
    }

    public SubPortfolioContentResponse updateSubPortfolioContent(SubPortfolioContentRequest request){
        SubPortfolioContentResponse resp = new SubPortfolioContentResponse();

        SubPortfolioContent subPortfolioContent = subPortfolioContentRepository.findBySubPortfolioId(UUID.fromString(request.getSubPortfolioId()));

        if (null == subPortfolioContent){
            // throw
        }

        String imagePath = saveImage(request.getImageUrl(), subPortfolioContent.getImageUrl());

        subPortfolioContent.setDescription(stringCheckUtil.isStringNEB(request.getDescription())? subPortfolioContent.getDescription() : request.getDescription());
        subPortfolioContent.setTitle(stringCheckUtil.isStringNEB(request.getTitle())? subPortfolioContent.getTitle() : request.getTitle());
        subPortfolioContent.setImageUrl(imagePath);
        subPortfolioContent.setUpdatedBy("user");
        subPortfolioContent.setUpdatedDate(dateTimeUtil.now());
        subPortfolioContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
        subPortfolioContentRepository.save(subPortfolioContent);

        BeanUtils.copyProperties(subPortfolioContent,resp);

        return resp;
    }


    private String saveImage(MultipartFile imageUrl, String existingUrl){
        String imagePath = "";

        try {
            if (null != imageUrl) {
                imagePath = imageUtil.saveImage(imageUrl);
            } else {
                imagePath = existingUrl;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return imagePath;
    }



}
