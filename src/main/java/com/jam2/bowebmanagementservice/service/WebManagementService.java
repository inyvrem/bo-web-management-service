package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SectionStatusEnum;
import com.jam2.bowebmanagementservice.constant.SystemConstants;
import com.jam2.bowebmanagementservice.entity.*;
import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.repository.*;
import com.jam2.bowebmanagementservice.util.DateTimeUtil;
import com.jam2.bowebmanagementservice.util.RawStringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class WebManagementService {

    private final UserWebRelationRepository userWebRelationRepository;
    private final AuthAccountRepository authAccountRepository;
    private final WebHeroContentRepository webHeroContentRepository;
    private final WebAboutContentRepository webAboutContentRepository;
    private final SubAboutContentRepository subAboutContentRepository;
    private final WebPortfolioContentRepository webPortfolioContentRepository;
    private final SubPortfolioContentRepository subPortfolioContentRepository;
    private final WebClientContentRepository webClientContentRepository;
    private final SubClientContentRepository subClientContentRepository;


    private final DateTimeUtil dateTimeUtil;
    private final RawStringUtil rawStringUtil;

    public WebManagementService(UserWebRelationRepository userWebRelationRepository,
                                AuthAccountRepository authAccountRepository, WebHeroContentRepository webHeroContentRepository,
                                WebAboutContentRepository webAboutContentRepository, SubAboutContentRepository subAboutContentRepository,
                                WebPortfolioContentRepository webPortfolioContentRepository, SubPortfolioContentRepository subPortfolioContentRepository,
                                WebClientContentRepository webClientContentRepository, SubClientContentRepository subClientContentRepository,
                                DateTimeUtil dateTimeUtil, RawStringUtil rawStringUtil) {
        this.userWebRelationRepository = userWebRelationRepository;
        this.authAccountRepository = authAccountRepository;
        this.webHeroContentRepository = webHeroContentRepository;
        this.webAboutContentRepository = webAboutContentRepository;
        this.subAboutContentRepository = subAboutContentRepository;
        this.webPortfolioContentRepository = webPortfolioContentRepository;
        this.subPortfolioContentRepository = subPortfolioContentRepository;
        this.webClientContentRepository = webClientContentRepository;
        this.subClientContentRepository = subClientContentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.rawStringUtil = rawStringUtil;
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
    public UserWebIdResponse  createUserWebRelation(String authId){
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
//    optional
        if (null == userWebRelation){
//            throw
        }

        WebHeroContent webHeroContent = createWebHeroContent(webCreateSectionRequest);
        WebAboutContent webAboutContent = createWebAboutContent(webCreateSectionRequest);
        WebPortfolioContent webPortfolioContent = createWebPortfolioContent(webCreateSectionRequest);
        WebClientContent webClientContent = createWebClientContent(webCreateSectionRequest);

        userWebRelation.setWebHeroId(webHeroContent.getWebHeroId());
        userWebRelation.setWebAboutId(webAboutContent.getWebAboutId());
        userWebRelation.setWebPortfolioId(webPortfolioContent.getWebPortfolioId());
        userWebRelation.setWebClientId(webClientContent.getWebClientId());

        userWebRelationRepository.save(userWebRelation);

        HashMap<String,Object> sectionList = new HashMap<>();

        sectionList.put("status", "Create successfully");


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

        webHeroContentRelationChecking(webUpdateSectionRequest, userWebRelation);
        webAboutContentRelationChecking(webUpdateSectionRequest,userWebRelation);
        webPortfolioContentRelationChecking(webUpdateSectionRequest,userWebRelation);
        webClientContentRelationChecking(webUpdateSectionRequest,userWebRelation);

        HashMap<String,Object> sectionList = new HashMap<>();

        sectionList.put("status", "update successfully");

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

        //optional
        UserWebRelation userWebRelation = userWebRelationRepository.findByAuthAccountAuthId(UUID.fromString(authId));
        if(null == userWebRelation){
            // throw
        }

        WebHeroContent webHeroContent = webHeroContentRepository.findByWebHeroIdAndSectionStatus(userWebRelation.getWebHeroId(),SectionStatusEnum.ACTIVE.getStatusCode());
        WebAboutContent webAboutContent = webAboutContentRepository.findByWebAboutIdAndSectionStatus(userWebRelation.getWebAboutId(),SectionStatusEnum.ACTIVE.getStatusCode());
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioIdAndSectionStatus(userWebRelation.getWebPortfolioId(),SectionStatusEnum.ACTIVE.getStatusCode());
        WebClientContent webClientContent= webClientContentRepository.findByWebClientIdAndSectionStatus(userWebRelation.getWebPortfolioId(),SectionStatusEnum.ACTIVE.getStatusCode());

        HashMap<String,Object> sectionList = new HashMap<>();
        if(null != webHeroContent) {
            WebHeroContentResponse webHeroContentResponse = new WebHeroContentResponse();
            BeanUtils.copyProperties(webHeroContent,webHeroContentResponse);
            sectionList.put("webHeroContent", webHeroContentResponse);
        }

        if(null != webAboutContent) {
            WebAboutContentResponse webAboutContentResponse = new WebAboutContentResponse();
            List<SubAboutContentResponse> subAboutContentResponseList = new ArrayList<>();
            List<SubAboutContent> subAboutContents = subAboutContentRepository.findByWebAboutContentWebAboutId(webAboutContent.getWebAboutId());
            BeanUtils.copyProperties(webAboutContent, webAboutContentResponse);
            if(subAboutContents.size() > 0){
                for(SubAboutContent aboutContent : subAboutContents){
                    SubAboutContentResponse response = new SubAboutContentResponse();
                    BeanUtils.copyProperties(aboutContent,response);
                    subAboutContentResponseList.add(response);
                }
            }
            webAboutContentResponse.setSubAboutContent(subAboutContentResponseList);
            sectionList.put("webAboutContent", webAboutContentResponse);
        }

        if(null != webPortfolioContent) {
            WebPortfolioContentResponse webPortfolioContentResponse = new WebPortfolioContentResponse();
            List<SubPortfolioContentResponse> subPortfolioContentResponseList = new ArrayList<>();
            List<SubPortfolioContent> subPortfolioContent = subPortfolioContentRepository.findByWebPortfolioContentWebPortfolioId(webPortfolioContent.getWebPortfolioId());
            BeanUtils.copyProperties(webPortfolioContent,webPortfolioContentResponse);
            if(subPortfolioContent.size() > 0){
                for(SubPortfolioContent portfolioContent : subPortfolioContent){
                    SubPortfolioContentResponse response = new SubPortfolioContentResponse();
                    BeanUtils.copyProperties(portfolioContent,response);
                    subPortfolioContentResponseList.add(response);
                }
            }
            webPortfolioContentResponse.setSubPortfolioContents(subPortfolioContentResponseList);
            sectionList.put("webPortfolioContent", webPortfolioContentResponse);
        }

        if(null != webClientContent) {
            WebClientContentResponse webClientContentResponse = new WebClientContentResponse();
            List<SubClientContentResponse> subClientContentResponseList = new ArrayList<>();
            List<SubClientContent> subClientContents = subClientContentRepository.findByWebClientContentWebClientId(webClientContent.getWebClientId());
            BeanUtils.copyProperties(webClientContent,webClientContentResponse);
            if(subClientContents.size() > 0){
                for(SubClientContent clientContent : subClientContents){
                    SubClientContentResponse response = new SubClientContentResponse();
                    BeanUtils.copyProperties(clientContent,response);
                    subClientContentResponseList.add(response);
                }
            }
            webClientContentResponse.setSubClientContents(subClientContentResponseList);
            sectionList.put("webClientContent", webClientContentResponse);
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
        webHeroContent.setSectionStatus(webUpdateSectionRequest.getIsWebHeroContentNeeded() ? SectionStatusEnum.ACTIVE.getStatusCode() : SectionStatusEnum.INACTIVE.getStatusCode());
        webHeroContent.setUpdatedBy(SystemConstants.SYSTEM);
        webHeroContent.setUpdatedDate(dateTimeUtil.now());
        webHeroContentRepository.save(webHeroContent);

        return webHeroContent;
    }

    private WebAboutContent webAboutContentRelationChecking(WebUpdateSectionRequest webUpdateSectionRequest, UserWebRelation userWebRelation){
        WebAboutContent webAboutContent = webAboutContentRepository.findByWebAboutId(userWebRelation.getWebAboutId());
        webAboutContent.setSectionStatus(webUpdateSectionRequest.getIsWebAboutContentNeeded() ? SectionStatusEnum.ACTIVE.getStatusCode() : SectionStatusEnum.INACTIVE.getStatusCode());
        webAboutContent.setCreatedBy(SystemConstants.SYSTEM);
        webAboutContent.setCreatedDate(dateTimeUtil.now());
        webAboutContentRepository.save(webAboutContent);

        return webAboutContent;
    }

    private WebPortfolioContent webPortfolioContentRelationChecking(WebUpdateSectionRequest webUpdateSectionRequest, UserWebRelation userWebRelation){
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(userWebRelation.getWebPortfolioId());
        webPortfolioContent.setSectionStatus(webUpdateSectionRequest.getIsWebPortfolioContentNeeded() ? SectionStatusEnum.ACTIVE.getStatusCode() : SectionStatusEnum.INACTIVE.getStatusCode());
        webPortfolioContent.setCreatedBy(SystemConstants.SYSTEM);
        webPortfolioContent.setCreatedDate(dateTimeUtil.now());
        webPortfolioContentRepository.save(webPortfolioContent);

        return webPortfolioContent;
    }

    private WebClientContent webClientContentRelationChecking(WebUpdateSectionRequest webUpdateSectionRequest, UserWebRelation userWebRelation){
        WebClientContent webClientContent = webClientContentRepository.findByWebClientId(userWebRelation.getWebClientId());
        webClientContent.setSectionStatus(webUpdateSectionRequest.getIsWebClientContentNeeded() ? SectionStatusEnum.ACTIVE.getStatusCode() : SectionStatusEnum.INACTIVE.getStatusCode());
        webClientContent.setCreatedBy(SystemConstants.SYSTEM);
        webClientContent.setCreatedDate(dateTimeUtil.now());
        webClientContentRepository.save(webClientContent);

        return webClientContent;
    }

    /**
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

    private WebClientContent createWebClientContent(WebCreateSectionRequest webCreateSectionRequest){
        WebClientContent webClientContent = new WebClientContent();

        webClientContent.setUserWebId(UUID.fromString(webCreateSectionRequest.getUserWebId()));
        webClientContent.setSectionStatus(webCreateSectionRequest.getIsWebPortfolioContentNeeded()?SectionStatusEnum.ACTIVE.getStatusCode():SectionStatusEnum.INACTIVE.getStatusCode());
        webClientContent.setCreatedBy(SystemConstants.SYSTEM);
        webClientContent.setCreatedDate(dateTimeUtil.now());
        webClientContentRepository.save(webClientContent);

        return webClientContent;
    }
}


