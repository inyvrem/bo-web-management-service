package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.WebHeroContentRequest;
import com.jam2.bowebmanagementservice.model.WebHeroContentResponse;
import com.jam2.bowebmanagementservice.model.WebIdResponse;
import com.jam2.bowebmanagementservice.service.WebHeroContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="back-office")
public class WebHeroController {
    // save image in cdn

    private final WebHeroContentService webHeroContentService;

    public WebHeroController(WebHeroContentService webHeroContentService) {
        this.webHeroContentService = webHeroContentService;
    }

    @GetMapping(value ="/public/web-manage/web-hero/{userWebId}")
    public ResponseEntity<?> getWebHeroId(@PathVariable("userWebId") String userWebId ){
        WebIdResponse webIdResponse = webHeroContentService.getWebHeroId(userWebId);
        return ResponseEntity.ok(webIdResponse);
    }

    @GetMapping(value ="/admin/web-manage/web-hero/details/{webHeroId}")
    public ResponseEntity<?> getWebHeroDetails(@PathVariable("webHeroId") String webHeroId ){
        WebHeroContentResponse webHeroContentResponse = webHeroContentService.getWebHeroContentDetail(webHeroId);
        return ResponseEntity.ok(webHeroContentResponse);
    }

    @PostMapping(value ="/secure/web-manage/web-hero/details/update")
    public ResponseEntity<?> updateWebHeroDetails( @RequestBody WebHeroContentRequest webHeroContentRequest){
        WebHeroContentResponse webHeroContentResponse = webHeroContentService.updateWebHeroContentDetail(webHeroContentRequest);
        return ResponseEntity.ok(webHeroContentResponse);
    }


}
