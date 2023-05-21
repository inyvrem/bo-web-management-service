package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SectionStatusEnum;
import com.jam2.bowebmanagementservice.constant.SystemConstants;
import com.jam2.bowebmanagementservice.entity.*;
import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.repository.*;
import com.jam2.bowebmanagementservice.util.DateTimeUtil;
import com.jam2.bowebmanagementservice.util.StringCheckUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class WebManagementService {

    private final UserWebRelationRepository userWebRelationRepository;
    private final AuthAccountRepository authAccountRepository;
    private final WebHeroContentRepository webHeroContentRepository;
    private final WebAboutContentRepository webAboutContentRepository;
    private final WebPortfolioContentRepository webPortfolioContentRepository;

    private final DateTimeUtil dateTimeUtil;
    private final StringCheckUtil stringCheckUtil;

    public WebManagementService(UserWebRelationRepository userWebRelationRepository, AuthAccountRepository authAccountRepository,
                                WebHeroContentRepository webHeroContentRepository, WebAboutContentRepository webAboutContentRepository,
                                WebPortfolioContentRepository webPortfolioContentRepository, DateTimeUtil dateTimeUtil, StringCheckUtil stringCheckUtil) {
        this.userWebRelationRepository = userWebRelationRepository;
        this.authAccountRepository = authAccountRepository;
        this.webHeroContentRepository = webHeroContentRepository;
        this.webAboutContentRepository = webAboutContentRepository;
        this.webPortfolioContentRepository = webPortfolioContentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.stringCheckUtil =stringCheckUtil;
    }

    public WebIdResponse getUserWebId(String authId){
        WebIdResponse resp = new WebIdResponse();
        UserWebRelation userWebRelation = userWebRelationRepository.findByAuthAccountAuthId(UUID.fromString(authId));

        if(null != userWebRelation){
            resp.setWebId(userWebRelation.getUserWebId().toString());
        }

        return resp;
    }


    /**
     *1st step to create the based relationship
     *
     */
    public UserWebIdResponse createUserWebRelation(String authId){
        UserWebRelation userWebRelation = new UserWebRelation();
        AuthAccount authAccount = authAccountRepository.findByAuthId(UUID.fromString(authId));

        if(null == authAccount){
            //throw
        }

        userWebRelation.setAuthAccount(authAccount);
        userWebRelationRepository.save(userWebRelation);

        return UserWebIdResponse.builder()
                .userWebId(userWebRelation.getUserWebId().toString())
                .build();
    }

    /**
     * second step  to create other section
     *
     * @param webCreateSectionRequest
     * @return
     */
    public WebCreateSectionResponse createSectionList(WebCreateSectionRequest webCreateSectionRequest){
        UserWebRelation userWebRelation = userWebRelationRepository.findByUserWebId(UUID.fromString(webCreateSectionRequest.getUserWebId()));

        if (null == userWebRelation){
//            throw
        }

        WebHeroContent webHeroContent = createWebHeroContent(webCreateSectionRequest);
        WebAboutContent webAboutContent = createWebAboutContent(webCreateSectionRequest);
        WebPortfolioContent webPortfolioContent = createWebPortfolioContent(webCreateSectionRequest);

        userWebRelation.setWebHeroId(webHeroContent.getWebHeroId());
        userWebRelation.setWebAboutId(webAboutContent.getWebAboutId());
        userWebRelation.setWebPortfolioId(webPortfolioContent.getWebPortfolioId());

        userWebRelationRepository.save(userWebRelation);

        HashMap<String,Object> sectionList = new HashMap<>();
        if(SectionStatusEnum.ACTIVE.getStatusCode() == webHeroContent.getSectionStatus()) {
            WebHeroContentResponse webHeroContentResponse = new WebHeroContentResponse();
            BeanUtils.copyProperties(webHeroContentResponse,webHeroContent);
            sectionList.put("webHeroContent", webHeroContentResponse);
        }

        if(SectionStatusEnum.ACTIVE.getStatusCode() == webAboutContent.getSectionStatus()) {
            WebAboutContentResponse webAboutContentResponse = new WebAboutContentResponse();
            BeanUtils.copyProperties(webAboutContentResponse,webAboutContent);
            sectionList.put("webAboutContent", webAboutContentResponse);
        }

        if(SectionStatusEnum.ACTIVE.getStatusCode() == webPortfolioContent.getSectionStatus()) {
            WebPortfolioContentResponse webPortfolioContentResponse = new WebPortfolioContentResponse();
            BeanUtils.copyProperties(webPortfolioContentResponse,webPortfolioContentResponse);
            sectionList.put("webPortfolioContent", webPortfolioContentResponse);
        }

        return WebCreateSectionResponse.builder()
                .authId(userWebRelation.getAuthAccount().getAuthId().toString())
                .username("asda")
                .sectionList(sectionList)
                .build();
    }

    /**
     *
     * Step 3  - to update the existing table
     *
     * @param webUpdateSectionRequest
     * @return
     */
    public WebUpdateSectionResponse updateSectionList(WebUpdateSectionRequest webUpdateSectionRequest){
        UserWebRelation userWebRelation = userWebRelationRepository.findByUserWebId(UUID.fromString(webUpdateSectionRequest.getUserWebId()));
        if(null == userWebRelation){
            //throw
        }

        WebHeroContent webHeroContent = webHeroContentRelationChecking(webUpdateSectionRequest, userWebRelation);
        WebAboutContent webAboutContent = webAboutContentRelationChecking(webUpdateSectionRequest,userWebRelation);
        WebPortfolioContent webPortfolioContent= webPortfolioContentRelationChecking(webUpdateSectionRequest,userWebRelation);

        HashMap<String,Object> sectionList = new HashMap<>();
        if(SectionStatusEnum.ACTIVE.getStatusCode() == webHeroContent.getSectionStatus()) {
            WebHeroContentResponse webHeroContentResponse = new WebHeroContentResponse();
            BeanUtils.copyProperties(webHeroContentResponse,webHeroContent);
            sectionList.put("webHeroContent", webHeroContentResponse);
        }

        if(SectionStatusEnum.ACTIVE.getStatusCode() == webAboutContent.getSectionStatus()) {
            WebAboutContentResponse webAboutContentResponse = new WebAboutContentResponse();
            BeanUtils.copyProperties(webAboutContentResponse,webAboutContent);
            sectionList.put("webAboutContent", webAboutContentResponse);
        }

        if(SectionStatusEnum.ACTIVE.getStatusCode() == webPortfolioContent.getSectionStatus()) {
            WebPortfolioContentResponse webPortfolioContentResponse = new WebPortfolioContentResponse();
            BeanUtils.copyProperties(webPortfolioContentResponse,webPortfolioContentResponse);
            sectionList.put("webPortfolioContent", webPortfolioContentResponse);
        }

        return WebUpdateSectionResponse.builder()
                .authId(userWebRelation.getAuthAccount().getAuthId().toString())
                .username(userWebRelation.getAuthAccount().getUsername())
                .sectionList(sectionList)
                .build();
    }

    /**
     *
     * chk section status by auth id
     *
     * @param authId
     * @return
     */
    public WebUpdateSectionResponse getUserSectionListById(String authId){
        AuthAccount authAccount = authAccountRepository.findByAuthId(UUID.fromString(authId));
        if(null == authAccount){
            //throw
        }

        UserWebRelation userWebRelation = userWebRelationRepository.findByAuthAccountAuthId(UUID.fromString(authId));
        if(null == userWebRelation){
            // throw
        }

        WebHeroContent webHeroContent = webHeroContentRepository.findByWebHeroIdAndSectionStatus(userWebRelation.getWebHeroId(),SectionStatusEnum.ACTIVE.getStatusCode());
        WebAboutContent webAboutContent = webAboutContentRepository.findByWebAboutIdAndSectionStatus(userWebRelation.getWebAboutId(),SectionStatusEnum.ACTIVE.getStatusCode());
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioIdAndSectionStatus(userWebRelation.getWebPortfolioId(),SectionStatusEnum.ACTIVE.getStatusCode());

        HashMap<String,Object> sectionList = new HashMap<>();
        if(null != webHeroContent) {
            WebHeroContentResponse webHeroContentResponse = new WebHeroContentResponse();
            BeanUtils.copyProperties(webHeroContentResponse,webHeroContent);
            sectionList.put("webHeroContent", webHeroContentResponse);
        }

        if(null != webAboutContent) {
            WebAboutContentResponse webAboutContentResponse = new WebAboutContentResponse();
            BeanUtils.copyProperties(webAboutContentResponse,webAboutContent);
            sectionList.put("webAboutContent", webAboutContentResponse);
        }

        if(null != webPortfolioContent) {
            WebPortfolioContentResponse webPortfolioContentResponse = new WebPortfolioContentResponse();
            BeanUtils.copyProperties(webPortfolioContentResponse,webPortfolioContentResponse);
            sectionList.put("webPortfolioContent", webPortfolioContentResponse);
        }

        return WebUpdateSectionResponse.builder()
                .authId(userWebRelation.getAuthAccount().getAuthId().toString())
                .username(authAccount.getUsername())
                .sectionList(sectionList)
                .build();

    }

    /**
     * UPDATE
     * if exist true status 1 => remain
     * if exist true status 3 => status update to 1
     * if exist false status 1 => status update to 3
     * if exist false status 3 => remain
     *
     *
     */
    private WebHeroContent webHeroContentRelationChecking(WebUpdateSectionRequest webUpdateSectionRequest, UserWebRelation userWebRelation){
        WebHeroContent webHeroContent = webHeroContentRepository.findByWebHeroId(userWebRelation.getWebHeroId());

      if (webUpdateSectionRequest.getIsWebHeroContentNeeded()){
          webHeroContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
          webHeroContent.setUpdatedBy(SystemConstants.SYSTEM);
          webHeroContent.setUpdatedDate(dateTimeUtil.now());
          webHeroContentRepository.save(webHeroContent);
       } else {
           webHeroContent.setSectionStatus(SectionStatusEnum.INACTIVE.getStatusCode());
           webHeroContent.setUpdatedBy(SystemConstants.SYSTEM);
           webHeroContent.setUpdatedDate(dateTimeUtil.now());
           webHeroContentRepository.save(webHeroContent);
       }

        return webHeroContent;
    }

    private WebAboutContent webAboutContentRelationChecking(WebUpdateSectionRequest webUpdateSectionRequest, UserWebRelation userWebRelation){
        WebAboutContent webAboutContent = webAboutContentRepository.findByWebAboutId(userWebRelation.getWebAboutId());

        if(webUpdateSectionRequest.getIsWebAboutContentNeeded()){
            webAboutContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
            webAboutContent.setCreatedBy(SystemConstants.SYSTEM);
            webAboutContent.setCreatedDate(dateTimeUtil.now());
            webAboutContentRepository.save(webAboutContent);
        } else{
            webAboutContent.setSectionStatus(SectionStatusEnum.INACTIVE.getStatusCode());
            webAboutContent.setUpdatedBy(SystemConstants.SYSTEM);
            webAboutContent.setUpdatedDate(dateTimeUtil.now());
            webAboutContentRepository.save(webAboutContent);
        }

        return webAboutContent;
    }

    private WebPortfolioContent webPortfolioContentRelationChecking(WebUpdateSectionRequest webUpdateSectionRequest, UserWebRelation userWebRelation){
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(userWebRelation.getWebPortfolioId());

        if(webUpdateSectionRequest.getIsWebPortfolioContentNeeded()){
            webPortfolioContent.setSectionStatus(SectionStatusEnum.ACTIVE.getStatusCode());
            webPortfolioContent.setCreatedBy(SystemConstants.SYSTEM);
            webPortfolioContent.setCreatedDate(dateTimeUtil.now());
            webPortfolioContentRepository.save(webPortfolioContent);
        }else {
            webPortfolioContent.setSectionStatus(SectionStatusEnum.INACTIVE.getStatusCode());
            webPortfolioContent.setUpdatedBy(SystemConstants.SYSTEM);
            webPortfolioContent.setUpdatedDate(dateTimeUtil.now());
            webPortfolioContentRepository.save(webPortfolioContent);
        }

        return webPortfolioContent;
    }

    /**
     *
     * Create Content
     */

    private WebHeroContent createWebHeroContent(WebCreateSectionRequest webCreateSectionRequest){
        WebHeroContent webHeroContent = new WebHeroContent();

        webHeroContent.setUserWebId(UUID.fromString(webCreateSectionRequest.getUserWebId()));
        webHeroContent.setSectionStatus(webCreateSectionRequest.getIsWebHeroContentNeeded()?SectionStatusEnum.ACTIVE.getStatusCode() : SectionStatusEnum.INACTIVE.getStatusCode());
        webHeroContent.setCreatedBy(SystemConstants.SYSTEM);
        webHeroContent.setCreatedDate(dateTimeUtil.now());
        webHeroContentRepository.save(webHeroContent);

        return webHeroContent;
    }

    private WebAboutContent createWebAboutContent(WebCreateSectionRequest webCreateSectionRequest){
        WebAboutContent webAboutContent = new WebAboutContent();

        webAboutContent.setUserWebId(UUID.fromString(webCreateSectionRequest.getUserWebId()));
        webAboutContent.setSectionStatus(webCreateSectionRequest.getIsWebAboutContentNeeded()?SectionStatusEnum.ACTIVE.getStatusCode():SectionStatusEnum.INACTIVE.getStatusCode());
        webAboutContent.setCreatedBy(SystemConstants.SYSTEM);
        webAboutContent.setCreatedDate(dateTimeUtil.now());
        webAboutContentRepository.save(webAboutContent);

        return webAboutContent;
    }

    private WebPortfolioContent createWebPortfolioContent(WebCreateSectionRequest webCreateSectionRequest){
        WebPortfolioContent webPortfolioContent = new WebPortfolioContent();

        webPortfolioContent.setUserWebId(UUID.fromString(webCreateSectionRequest.getUserWebId()));
        webPortfolioContent.setSectionStatus(webCreateSectionRequest.getIsWebPortfolioContentNeeded()?SectionStatusEnum.ACTIVE.getStatusCode():SectionStatusEnum.INACTIVE.getStatusCode());
        webPortfolioContent.setCreatedBy(SystemConstants.SYSTEM);
        webPortfolioContent.setCreatedDate(dateTimeUtil.now());
        webPortfolioContentRepository.save(webPortfolioContent);

        return webPortfolioContent;
    }
}


