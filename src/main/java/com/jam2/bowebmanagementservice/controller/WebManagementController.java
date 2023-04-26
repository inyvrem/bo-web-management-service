package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.UserWebIdResponse;
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
    public ResponseEntity<?> getWebId(@PathVariable("authId") String authId ){
        UserWebIdResponse userWebIdResponse = webManagementService.getUserWebId(authId);
        return ResponseEntity.ok(userWebIdResponse);
    }

}
