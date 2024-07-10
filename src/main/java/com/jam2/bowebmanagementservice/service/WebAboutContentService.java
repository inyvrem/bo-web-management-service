package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SectionStatusEnum;
import com.jam2.bowebmanagementservice.entity.SubAboutContent;
import com.jam2.bowebmanagementservice.entity.SubPortfolioContent;
import com.jam2.bowebmanagementservice.entity.WebAboutContent;
import com.jam2.bowebmanagementservice.entity.WebPortfolioContent;
import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.repository.SubAboutContentRepository;
import com.jam2.bowebmanagementservice.repository.WebAboutContentRepository;
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
public class WebAboutContentService {

    private final WebAboutContentRepository webAboutContentRepository;
    private final SubAboutContentRepository subAboutContentRepository;
    private final DateTimeUtil dateTimeUtil;
    private final RawStringUtil rawStringUtil;
    private final ImageUtil imageUtil;

    public WebAboutContentService(WebAboutContentRepository webAboutContentRepository, SubAboutContentRepository subAboutContentRepository,
                                  DateTimeUtil dateTimeUtil, RawStringUtil rawStringUtil,
                                  ImageUtil imageUtil) {
        this.webAboutContentRepository = webAboutContentRepository;
        this.subAboutContentRepository = subAboutContentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.rawStringUtil = rawStringUtil;
        this.imageUtil = imageUtil;
    }

    public WebIdResponse getWebAboutId(String userWebId){
        WebIdResponse resp = new WebIdResponse();
        WebAboutContent webAboutContent = webAboutContentRepository.findByUserWebId(UUID.fromString(userWebId));

        if(null != webAboutContent){
            resp.setWebId(webAboutContent.getWebAboutId().toString());
        }

        return resp;
    }

    public WebAboutContentResponse getWebAboutContentDetail(String webAboutId){
        WebAboutContentResponse resp = new WebAboutContentResponse();
        List<SubAboutContentResponse> subAboutContentResponsesList = new ArrayList<>();
        WebAboutContent webAboutContent = webAboutContentRepository.findByWebAboutId(UUID.fromString(webAboutId));
        List<SubAboutContent> subAboutContents =
                subAboutContentRepository.findBySectionStatusAndWebAboutContentWebAboutId(SectionStatusEnum.ACTIVE.getStatusCode(), UUID.fromString(webAboutId));

        if(null != subAboutContents){
            for(SubAboutContent subAboutContent : subAboutContents){
                SubAboutContentResponse subResp = new SubAboutContentResponse();
                BeanUtils.copyProperties(subAboutContent,subResp);

                subAboutContentResponsesList.add(subResp);
            }
        }

        if(null != webAboutContent){
            resp.setWebAboutId(webAboutContent.getWebAboutId());
            resp.setDescription(webAboutContent.getDescription());
            resp.setImageUrl(webAboutContent.getImageUrl());
            resp.setTitle(webAboutContent.getTitle());
            resp.setSubAboutContent(subAboutContentResponsesList);

        }

        return resp;
    }

    public WebAboutContentResponse updateWebAboutContentDetail(WebAboutContentRequest webAboutContentRequest){
        WebAboutContentResponse resp = new WebAboutContentResponse();
        if(null == webAboutContentRequest){
//            throw
        }

        WebAboutContent webAboutContent = webAboutContentRepository.findByWebAboutId(UUID.fromString(webAboutContentRequest.getWebAboutId()));

        if(null == webAboutContent){
           // throw
        }

        String imagePath = "";
        imagePath = webAboutContent.getImageUrl();

//        try {
//            if (null != webAboutContentRequest.getImageUrl()) {
//                imagePath = imageUtil.saveImage(webAboutContentRequest.getImageUrl());
//            } else {
//                imagePath = webAboutContent.getImageUrl();
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

        webAboutContent.setTitle(rawStringUtil.isStringNEB(webAboutContentRequest.getTitle()) ? webAboutContent.getTitle() : webAboutContentRequest.getTitle());
        webAboutContent.setDescription(rawStringUtil.isStringNEB(webAboutContentRequest.getDescription()) ? webAboutContent.getDescription(): webAboutContentRequest.getDescription());
        webAboutContent.setImageUrl(imagePath);
        webAboutContent.setUpdatedBy("user"); // add token user
        webAboutContent.setUpdatedDate(dateTimeUtil.now());
        webAboutContentRepository.save(webAboutContent);

        BeanUtils.copyProperties(webAboutContent,resp);

        return resp;
    }

    public void deleteSubAboutContentDetail(String subAboutId){

        SubAboutContent subAboutContent = subAboutContentRepository.findBySubAboutId(UUID.fromString(subAboutId));

        if (null == subAboutContent){
            // unable to delete
        }

        subAboutContent.setSectionStatus(SectionStatusEnum.DELETE.getStatusCode());
        subAboutContentRepository.save(subAboutContent);

    }

    public void inactiveSubAboutContentDetail(String subAboutId){

        SubAboutContent subAboutContent = subAboutContentRepository.findBySubAboutId(UUID.fromString(subAboutId));

        if (null == subAboutContent){
            // unable to delete
        }

        subAboutContent.setSectionStatus(SectionStatusEnum.INACTIVE.getStatusCode());
        subAboutContentRepository.save(subAboutContent);

    }

    public HashMap<String,Object> createSubAboutContent(SubAboutContentCreateRequest request){

        List<SubAboutContent> subAboutContentList= subAboutContentRepository.findBySectionStatusAndWebAboutContentWebAboutId(SectionStatusEnum.ACTIVE.getStatusCode(),UUID.fromString(request.getWebAboutId()));
        WebAboutContent webAboutContent = webAboutContentRepository.findByWebAboutId(UUID.fromString(request.getWebAboutId()));

        if(null != subAboutContentList && subAboutContentList.size() >= 6){
//                throw
        }

//        String imagePath = saveImage(request.getImageUrl(), webAboutContent.getImageUrl());

        SubAboutContent subAboutContent = SubAboutContent.builder()
                .title(request.getTitle())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .createdDate(dateTimeUtil.now())
                .createdBy("User")
                .sectionStatus(SectionStatusEnum.ACTIVE.getStatusCode())
                .webAboutContent(webAboutContent)
                .build();

        subAboutContentRepository.save(subAboutContent);

        HashMap<String,Object> resp = new HashMap<>();

        resp.put("status", "update successfully");


        return resp;
    }

    public SubAboutContentResponse updateSubAboutContent(SubAboutContentRequest request){
        SubAboutContentResponse resp = new SubAboutContentResponse();

        if(null == request.getSubAboutId()){
            System.out.println(request);
        }

        SubAboutContent subAboutContent = subAboutContentRepository.findBySubAboutId(UUID.fromString(request.getSubAboutId()));

        if (null == subAboutContent){
            // throw
        }

//        String imagePath = saveImage(request.getImageUrl(), subAboutContent.getImageUrl());

        subAboutContent.setDescription(rawStringUtil.isStringNEB(request.getDescription())? subAboutContent.getDescription() : request.getDescription());
        subAboutContent.setTitle(rawStringUtil.isStringNEB(request.getTitle())? subAboutContent.getTitle() : request.getTitle());
        subAboutContent.setImageUrl(rawStringUtil.isStringNEB(request.getImageUrl())? subAboutContent.getImageUrl() : request.getImageUrl());
        subAboutContent.setUpdatedBy("user");
        subAboutContent.setUpdatedDate(dateTimeUtil.now());
        subAboutContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
        subAboutContentRepository.save(subAboutContent);

        BeanUtils.copyProperties(subAboutContent,resp);

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
