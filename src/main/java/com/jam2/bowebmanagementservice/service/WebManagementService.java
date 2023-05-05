package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.entity.AuthAccount;
import com.jam2.bowebmanagementservice.entity.UserWebRelation;
import com.jam2.bowebmanagementservice.entity.WebHeroContent;
import com.jam2.bowebmanagementservice.model.UserWebIdResponse;
import com.jam2.bowebmanagementservice.model.WebIdResponse;
import com.jam2.bowebmanagementservice.model.WebSectionListRequest;
import com.jam2.bowebmanagementservice.model.WebSectionListResponse;
import com.jam2.bowebmanagementservice.repository.AuthAccountRepository;
import com.jam2.bowebmanagementservice.repository.UserWebRelationRepository;
import com.jam2.bowebmanagementservice.repository.WebHeroContentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebManagementService {

    private final UserWebRelationRepository userWebRelationRepository;
    private final AuthAccountRepository authAccountRepository;
    private final WebHeroContentRepository webHeroContentRepository;

    public WebManagementService(UserWebRelationRepository userWebRelationRepository, AuthAccountRepository authAccountRepository,
                                WebHeroContentRepository webHeroContentRepository) {
        this.userWebRelationRepository = userWebRelationRepository;
        this.authAccountRepository = authAccountRepository;
        this.webHeroContentRepository = webHeroContentRepository;
    }

    public WebIdResponse getUserWebId(String authId){
        WebIdResponse resp = new WebIdResponse();
        UserWebRelation userWebRelation = userWebRelationRepository.findByAuthAccountAuthId(UUID.fromString(authId));

        if(null != userWebRelation){
            resp.setWebId(userWebRelation.getUserWebId().toString());
        }

        return resp;
    }


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

    public WebSectionListResponse createUserSectionList(WebSectionListRequest webHeroContentRequest){
        UserWebRelation userWebRelation = userWebRelationRepository.findByUserWebId(UUID.fromString(webHeroContentRequest.getUserWebId()));
        if(null == userWebRelation){
            //throw
        }

        WebHeroContent webHeroContent = webHeroContentRelationChecking(webHeroContentRequest.getWebHeroContent(), userWebRelation);
        userWebRelation.setWebHeroId(null != webHeroContent.getWebHeroId()? webHeroContent.getWebHeroId() : null);
        userWebRelationRepository.save(userWebRelation);

        return WebSectionListResponse.builder()
                .authId(userWebRelation.getAuthAccount().getAuthId().toString())
                .username(userWebRelation.getAuthAccount().getUsername())
                .webHeroContent(null != userWebRelation.getWebHeroId()?  userWebRelation.getWebHeroId().toString() : null)
                .webAboutContent(null != userWebRelation.getWebAboutId()? userWebRelation.getWebAboutId().toString() : null)
                .build();
    }

    public WebSectionListResponse getUserSectionList(String authId){
        AuthAccount authAccount = authAccountRepository.findByAuthId(UUID.fromString(authId));
        if(null == authAccount){
            //throw
        }

        UserWebRelation userWebRelation = userWebRelationRepository.findByAuthAccountAuthId(UUID.fromString(authId));
        if(null == userWebRelation){
            // throw
        }

        return WebSectionListResponse.builder()
                .authId(userWebRelation.getAuthAccount().getAuthId().toString())
                .username(authAccount.getUsername())
                .webHeroContent(null != userWebRelation.getWebHeroId() ? userWebRelation.getWebHeroId().toString() : null)
                .webAboutContent(null != userWebRelation.getWebAboutId() ? userWebRelation.getWebAboutId().toString() : null)
                .build();

    }

    private WebHeroContent webHeroContentRelationChecking(Boolean webHeroContentBoolean, UserWebRelation userWebRelation){
        WebHeroContent webHeroContent = new WebHeroContent();
        if( null != userWebRelation.getWebHeroId()){
            //throw exist
        }

        if(null != webHeroContentBoolean && webHeroContentBoolean == true){
            webHeroContent.setUserWebId(userWebRelation.getUserWebId());
            webHeroContentRepository.save(webHeroContent);
        }

        return webHeroContent;
    }

}
