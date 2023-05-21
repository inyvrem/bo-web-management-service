package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.constant.SystemConstants;
import com.jam2.bowebmanagementservice.entity.WebHeroContent;
import com.jam2.bowebmanagementservice.model.WebHeroContentRequest;
import com.jam2.bowebmanagementservice.model.WebHeroContentResponse;
import com.jam2.bowebmanagementservice.model.WebIdResponse;
import com.jam2.bowebmanagementservice.repository.UserWebRelationRepository;
import com.jam2.bowebmanagementservice.repository.WebHeroContentRepository;
import com.jam2.bowebmanagementservice.util.DateTimeUtil;
import com.jam2.bowebmanagementservice.util.StringCheckUtil;
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
    private final DateTimeUtil dateTimeUtil;
    private final StringCheckUtil stringCheckUtil;

    public WebHeroContentService(WebHeroContentRepository webHeroContentRepository, DateTimeUtil dateTimeUtil, StringCheckUtil stringCheckUtil) {
        this.webHeroContentRepository = webHeroContentRepository;
        this.dateTimeUtil = dateTimeUtil;
        this.stringCheckUtil = stringCheckUtil;
    }

    public WebIdResponse getWebHeroId(String userWebId){
        WebIdResponse resp = new WebIdResponse();
        WebHeroContent webHeroContent = webHeroContentRepository.findByUserWebId(UUID.fromString(userWebId));

        if(null != webHeroContent){
            resp.setWebId(webHeroContent.getWebHeroId().toString());
        }

        return resp;
    }

    public WebHeroContentResponse getWebHeroContentDetail(String webHeroId){
        WebHeroContentResponse resp = new WebHeroContentResponse();
        WebHeroContent webHeroContent = webHeroContentRepository.findByWebHeroId(UUID.fromString(webHeroId));

        if(null != webHeroContent){
            BeanUtils.copyProperties(webHeroContent,resp);
        }

        return resp;
    }

    public WebHeroContentResponse updateWebHeroContentDetail(WebHeroContentRequest webHeroContentRequest){
        WebHeroContentResponse resp = new WebHeroContentResponse();
        WebHeroContent webHeroContent = webHeroContentRepository.findByWebHeroId(UUID.fromString(webHeroContentRequest.getWebHeroId()));

        if(null == webHeroContent){
           // throw
        }

        webHeroContent.setTitle(stringCheckUtil.isStringNEB(webHeroContentRequest.getTitle()) ? webHeroContent.getTitle() : webHeroContentRequest.getTitle());
        webHeroContent.setDescription(stringCheckUtil.isStringNEB(webHeroContentRequest.getDescription()) ? webHeroContent.getDescription(): webHeroContentRequest.getDescription());
        webHeroContent.setImageUrl( stringCheckUtil.isStringNEB(webHeroContentRequest.getImageUrl()) ? webHeroContent.getImageUrl() : webHeroContentRequest.getImageUrl());
        webHeroContent.setUpdatedBy("user"); // add token user
        webHeroContent.setUpdatedDate(dateTimeUtil.now());
        webHeroContentRepository.save(webHeroContent);

        BeanUtils.copyProperties(webHeroContent,resp);

        return resp;
    }

}
