package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.WebHeroContentResponse;
import com.jam2.bowebmanagementservice.model.WebIdResponse;
import com.jam2.bowebmanagementservice.service.WebHeroContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="back-office")
public class WebPortfolioController {
    /**
     * create webportfolio
     * get webportfolio
     * update portfolio
     *
     * create sub portfolio -> one by one button
     * delete sub portfolio -> button to delte
     * update sub portfolio -> {list can delete one to many }
     * get sub portfolio -> all
     * get sub portfolio -> one  -
     *
     */

//    private final WebHeroContentService webHeroContentService;
//
//    public WebPortfolioController(WebHeroContentService webHeroContentService) {
//        this.webHeroContentService = webHeroContentService;
//    }
//
//    @GetMapping(value ="/public/web-manage/web-hero/{userWebId}")
//    public ResponseEntity<?> getWebHeroId(@PathVariable("userWebId") String userWebId ){
//        WebIdResponse webIdResponse = webHeroContentService.getWebHeroId(userWebId);
//        return ResponseEntity.ok(webIdResponse);
//    }
//
//    @GetMapping(value ="/admin/web-manage/web-hero/details/{webHeroId}")
//    public ResponseEntity<?> getWebHeroDetails(@PathVariable("webHeroId") String webHeroId ){
//        WebHeroContentResponse webHeroContentResponse = webHeroContentService.getUserWebDetail(webHeroId);
//        return ResponseEntity.ok(webHeroContentResponse);
//    }

//    @PostMapping(value ="/secure/web-manage/web-hero/details/create")
//    public ResponseEntity<?> createWebHeroDetails( @RequestBody WebHeroContentRequest webHeroContentRequest){
//        WebHeroContentResponse webHeroContentResponse = webHeroContentService.createUserDetail(webHeroContentRequest);
//        return ResponseEntity.ok(userDetailResponse);
//    }


}
