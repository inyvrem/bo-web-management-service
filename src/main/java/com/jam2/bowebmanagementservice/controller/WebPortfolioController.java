package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.service.WebPortfolioContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value ="back-office")
public class WebPortfolioController {
    /**
     * create webportfolio
     * get webportfolio
     * update portfolio
     *
     * create sub portfolio -> one by one button
     * delete sub portfolio -> button to delete
     * update sub portfolio -> {list can delete one to many }
     * get sub portfolio -> all
     * get sub portfolio -> one  -
     *
     */

    private final WebPortfolioContentService webPortfolioContentService;

    public WebPortfolioController(WebPortfolioContentService webPortfolioContentService) {
        this.webPortfolioContentService = webPortfolioContentService;
    }

    @GetMapping(value ="/public/web-manage/web-portfolio/{userWebId}")
    public ResponseEntity<?> getWebPortfolioId(@PathVariable("userWebId") String userWebId ){
        WebIdResponse webIdResponse = webPortfolioContentService.getWebPortfolioId(userWebId);
        return ResponseEntity.ok(webIdResponse);
    }

    @GetMapping(value ="/admin/web-manage/web-portfolio/details/{webPortfolioId}")
    public ResponseEntity<?> getWebPortfolioDetails(@PathVariable("webPortfolioId") String webPortfolioId ){
        WebPortfolioContentResponse webPortfolioContentResponse = webPortfolioContentService.getWebPortfolioContentDetail(webPortfolioId);
        return ResponseEntity.ok(webPortfolioContentResponse);
    }

    @PostMapping(value ="/secure/web-manage/web-portfolio/details/update")
    public ResponseEntity<?> updateWebPortfolioDetails( @RequestBody WebPortfolioContentCreateRequest webPortfolioContentRequest){
        HashMap<String,Object> resp = webPortfolioContentService.updateWebPortfolioContentDetail(webPortfolioContentRequest);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value ="/secure/web-manage/web-portfolio/sub-portfolio/create")
    public ResponseEntity<?> createSubPortfolioContentDetail( @RequestBody SubPortfolioContentCreateRequest request){
        HashMap<String,Object> resp = webPortfolioContentService.createSubPortfolioContent(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value ="/secure/web-manage/web-portfolio/sub-portfolio/update")
    public ResponseEntity<?> updateSubPortfolioContentDetail( @RequestBody SubPortfolioContentRequest subPortfolioContentRequest){
        SubPortfolioContentResponse subPortfolioContentResponse = webPortfolioContentService.updateSubPortfolioContent(subPortfolioContentRequest);
        return ResponseEntity.ok(subPortfolioContentResponse);
    }

    @PatchMapping(value ="/secure/web-manage/web-portfolio/sub-portfolio/delete/{subPortfolioId}")
    public ResponseEntity<?> deleteSubPortfolioContentDetail( @PathVariable("subPortfolioId") String subPortfolioId){
         webPortfolioContentService.deleteSubPortfolioContentDetail(subPortfolioId);
        return ResponseEntity.ok("Deleted");
    }

    @PatchMapping(value ="/secure/web-manage/web-portfolio/sub-portfolio/inactive/{subPortfolioId}")
    public ResponseEntity<?> inactiveSubPortfolioContentDetail( @PathVariable("subPortfolioId") String subPortfolioId){
        webPortfolioContentService.inactiveSubPortfolioContentDetail(subPortfolioId);
        return ResponseEntity.ok("Deleted");
    }


}
