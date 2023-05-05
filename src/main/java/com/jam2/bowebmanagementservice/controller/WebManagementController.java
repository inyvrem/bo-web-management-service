package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.service.WebManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="back-office")
public class WebManagementController {

    /**
     * get webid using auth id
     * get specific id using web id
     * set using specific id
     */

    private final WebManagementService webManagementService;

    public WebManagementController(WebManagementService webManagementService) {
        this.webManagementService = webManagementService;
    }

    @GetMapping(value ="/admin/web-manage/{authId}")
    public ResponseEntity<?> getUserWebId(@PathVariable("authId") String authId ){
        WebIdResponse webIdResponse = webManagementService.getUserWebId(authId);
        return ResponseEntity.ok(webIdResponse);
    }

    @PostMapping(value ="/admin/web-manage/web-relation/{authId}")
    public ResponseEntity<?> createUserWebRelation(@PathVariable("authId") String authId){
        UserWebIdResponse userWebIdResponse = webManagementService.createUserWebRelation(authId);
        return ResponseEntity.ok(userWebIdResponse);
    }

    @PostMapping(value ="/admin/web-manage/section-list")
    public ResponseEntity<?> createUserSectionList(@RequestBody WebSectionListRequest webHeroContentRequest){
        WebSectionListResponse webSectionListResponse = webManagementService.createUserSectionList(webHeroContentRequest);
        return ResponseEntity.ok(webSectionListResponse);
    }

    @GetMapping(value ="/admin/web-manage/section-list/{authId}")
    public ResponseEntity<?> getUserSectionList( @PathVariable("authId") String authId ){
        WebSectionListResponse webSectionListResponse = webManagementService.getUserSectionList(authId);
        return ResponseEntity.ok(webSectionListResponse);
    }

    //update

}
