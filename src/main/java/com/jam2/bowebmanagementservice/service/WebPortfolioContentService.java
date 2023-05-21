package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.entity.WebPortfolioContent;
import com.jam2.bowebmanagementservice.model.WebIdResponse;
import com.jam2.bowebmanagementservice.model.WebPortfolioContentResponse;
import com.jam2.bowebmanagementservice.repository.SubPortfolioContentRepository;
import com.jam2.bowebmanagementservice.repository.WebPortfolioContentRepository;
import com.jam2.bowebmanagementservice.util.DateTimeUtil;
import com.jam2.bowebmanagementservice.util.StringCheckUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        WebPortfolioContent webPortfolioContent = webPortfolioContentRepository.findByWebPortfolioId(UUID.fromString(webPortfolioId));

        if(null != webPortfolioContent){
            BeanUtils.copyProperties(webPortfolioContent,resp);
        }

        return resp;
    }



//    public WebPortfolioContentResponse updateWebPortfolioContentDetail(WebHeroContentRequest webHeroContentRequest){
//        WebHeroContentResponse resp = new WebHeroContentResponse();
//        WebHeroContent webHeroContent = webHeroContentRepository.findByWebHeroId(UUID.fromString(webHeroContentRequest.getWebHeroId()));
//
//        if(null == webHeroContent){
//           // throw
//        }
//
//        webHeroContent.setTitle(webHeroContentRequest.getTitle() == null ? SystemConstants.TITLE : webHeroContentRequest.getTitle());
//        webHeroContent.setDescription(webHeroContentRequest.getDescription() == null ? SystemConstants.DESCRIPTION : webHeroContentRequest.getDescription());
//        webHeroContent.setImageUrl(webHeroContentRequest.getImageUrl() == null ? SystemConstants.IMAGE_URL : webHeroContentRequest.getImageUrl());
//        webHeroContent.setUpdatedBy("user"); // add token user
//        webHeroContent.setUpdatedDate(dateTimeUtil.now());
//        webHeroContentRepository.save(webHeroContent);
//
//        BeanUtils.copyProperties(webHeroContent,resp);
//
//        return resp;
//    }

}
