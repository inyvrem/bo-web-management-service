package com.jam2.bowebmanagementservice.service;

import com.jam2.bowebmanagementservice.entity.UserWebRelation;
import com.jam2.bowebmanagementservice.model.UserWebIdResponse;
import com.jam2.bowebmanagementservice.repository.UserWebRelationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebManagementService {

    private final UserWebRelationRepository userWebRelationRepository;

    public WebManagementService(UserWebRelationRepository userWebRelationRepository) {
        this.userWebRelationRepository = userWebRelationRepository;
    }

    public UserWebIdResponse getUserWebId(String authId){
        UserWebIdResponse resp = new UserWebIdResponse();
        UserWebRelation userWebRelation = userWebRelationRepository.findByAuthId(UUID.fromString(authId));

        if(null != userWebRelation){
            resp.setUserWebId(userWebRelation.getUserWebId().toString());
        }

        return resp;
    }

}
