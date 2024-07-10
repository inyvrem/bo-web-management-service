package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.service.WebAboutContentService;
import com.jam2.bowebmanagementservice.service.WebHeroContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value ="back-office")
public class WebAboutController {
    // save image in cdn

    private final WebAboutContentService webAboutContentService;

    public WebAboutController(WebAboutContentService webAboutContentService) {
        this.webAboutContentService = webAboutContentService;
    }

    @GetMapping(value ="/public/web-manage/web-about/{userWebId}")
    public ResponseEntity<?> getWebAboutId(@PathVariable("userWebId") String userWebId ){
        WebIdResponse webIdResponse = webAboutContentService.getWebAboutId(userWebId);
        return ResponseEntity.ok(webIdResponse);
    }

    @GetMapping(value ="/admin/web-manage/web-about/details/{webAboutId}")
    public ResponseEntity<?> getWebAboutDetails(@PathVariable("webAboutId") String webAboutId ){
        WebAboutContentResponse webAboutContentResponse = webAboutContentService.getWebAboutContentDetail(webAboutId);
        return ResponseEntity.ok(webAboutContentResponse);
    }

    @PostMapping(value ="/secure/web-manage/web-about/details/update")
    public ResponseEntity<?> updateWebAboutDetails( @RequestBody WebAboutContentRequest webAboutContentRequest){
        WebAboutContentResponse webAboutContentResponse = webAboutContentService.updateWebAboutContentDetail(webAboutContentRequest);
        return ResponseEntity.ok(webAboutContentResponse);
    }

    @PostMapping(value ="/secure/web-manage/web-about/sub-about/create")
    public ResponseEntity<?> createSubAboutContentDetail( @RequestBody SubAboutContentCreateRequest request){
        HashMap<String,Object> resp = webAboutContentService.createSubAboutContent(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value ="/secure/web-manage/web-about/sub-about/update")
    public ResponseEntity<?> updateSubAboutContentDetail( @RequestBody SubAboutContentRequest subAboutContentRequest){
        SubAboutContentResponse subAboutContentResponse = webAboutContentService.updateSubAboutContent(subAboutContentRequest);
        return ResponseEntity.ok(subAboutContentResponse);
    }

    @PatchMapping(value ="/secure/web-manage/web-about/sub-about/delete/{subAboutId}")
    public ResponseEntity<?> deleteSubAboutContentDetail( @PathVariable("subAboutId") String subAboutId){
        webAboutContentService.inactiveSubAboutContentDetail(subAboutId);
        return ResponseEntity.ok("Deleted");
    }


}
