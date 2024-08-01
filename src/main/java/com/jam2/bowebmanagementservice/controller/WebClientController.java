package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.service.WebAboutContentService;
import com.jam2.bowebmanagementservice.service.WebClientContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value ="back-office")
public class WebClientController {
    // save image in cdn

    private final WebClientContentService webClientContentService;

    public WebClientController(WebClientContentService webClientContentService) {
        this.webClientContentService = webClientContentService;
    }

    @GetMapping(value ="/public/web-manage/web-client/{userWebId}")
    public ResponseEntity<?> getWebClientId(@PathVariable("userWebId") String userWebId ){
        WebIdResponse webIdResponse = webClientContentService.getWebClientId(userWebId);
        return ResponseEntity.ok(webIdResponse);
    }

    @GetMapping(value ="/admin/web-manage/web-client/details/{webClientId}")
    public ResponseEntity<?> getWebClientDetails(@PathVariable("webClientId") String webClientId ){
        WebClientContentResponse webClientContentResponse = webClientContentService.getWebClientContentDetail(webClientId);
        return ResponseEntity.ok(webClientContentResponse);
    }

    @PostMapping(value ="/secure/web-manage/web-client/details/update")
    public ResponseEntity<?> updateWebClientDetails( @RequestBody WebClientContentRequest webClientContentRequest){
        WebClientContentResponse webClientContentResponse = webClientContentService.updateWebClientContentDetail(webClientContentRequest);
        return ResponseEntity.ok(webClientContentResponse);
    }

    @PostMapping(value ="/secure/web-manage/web-client/sub-client/create")
    public ResponseEntity<?> createSubClientContentDetail( @RequestBody SubClientCreateContentRequest request){
        HashMap<String,Object> resp = webClientContentService.createSubClientContent(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value ="/secure/web-manage/web-client/sub-client/update")
    public ResponseEntity<?> updateSubClientContentDetail( @RequestBody SubClientContentRequest request){
        SubClientContentResponse resp = webClientContentService.updateSubClientContent(request);
        return ResponseEntity.ok(resp);
    }

    @PatchMapping(value ="/secure/web-manage/web-client/sub-client/delete/{subClientId}")
    public ResponseEntity<?> deleteSubClientContentDetail( @PathVariable("subClientId") String subClientId){
        webClientContentService.inactiveSubClientContentDetail(subClientId);
        return ResponseEntity.ok("Deleted");
    }


}
