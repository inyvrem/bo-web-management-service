package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SystemConstants;
import com.jam2.bowebmanagementservice.entity.UserWebRelation;
import com.jam2.bowebmanagementservice.entity.WebHeroContent;
import com.jam2.bowebmanagementservice.model.WebHeroContentRequest;
import com.jam2.bowebmanagementservice.model.WebHeroContentResponse;
import com.jam2.bowebmanagementservice.model.WebIdResponse;
import com.jam2.bowebmanagementservice.repository.UserWebRelationRepository;
import com.jam2.bowebmanagementservice.repository.WebHeroContentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * get webHeroId
 * get webHeroContent
 * post/ set web hero content
 *
 *
 */
@Service
public class WebHeroContentService {

    private final WebHeroContentRepository webHeroContentRepository;
    private final UserWebRelationRepository userWebRelationRepository;

    public WebHeroContentService(WebHeroContentRepository webHeroContentRepository, UserWebRelationRepository userWebRelationRepository) {
        this.webHeroContentRepository = webHeroContentRepository;
        this.userWebRelationRepository = userWebRelationRepository;
    }

    public WebIdResponse getWebHeroId(String userWebId){
        WebIdResponse resp = new WebIdResponse();
        WebHeroContent webHeroContent = webHeroContentRepository.findByUserWebId(UUID.fromString(userWebId));

        if(null != webHeroContent){
            resp.setWebId(webHeroContent.getWebHeroId().toString());
        }

        return resp;
    }

    public WebHeroContentResponse getUserWebDetail(String webHeroId){
        WebHeroContentResponse resp = new WebHeroContentResponse();
        WebHeroContent webHeroContent = webHeroContentRepository.findByWebHeroId(UUID.fromString(webHeroId));

        if(null != webHeroContent){
            BeanUtils.copyProperties(webHeroContent,resp);
        }

        return resp;
    }

    public WebHeroContentResponse createWebHeroContentDetails(WebHeroContentRequest webHeroContentRequest){
        WebHeroContentResponse resp = new WebHeroContentResponse();
        WebHeroContent webHeroContent = new WebHeroContent();

//        UserWebRelation userWebRelation = userWebRelationRepository.findByAuthAccountAuthId();

//        if(null == webHeroContent){
//            webHeroContent.setTitle(webHeroContentRequest.getTitle() == null ? SystemConstants.TITLE : webHeroContentRequest.getTitle());
//            webHeroContent.setDescription();
//            webHeroContent.setImageUrl();
//            webHeroContent.setCreatedBy();
//            webHeroContent.setCreatedDate();
//            webHeroContent.setUserWebRelation();
//        }

        return resp;
    }

}
