package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.model.*;
import com.jam2.bowebmanagementservice.service.WebManagementService;
import com.jam2.bowebmanagementservice.util.RawStringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value ="back-office")
@Tag(name = "Web Management Controller", description = "REST APIs related to Sample Entity")
public class WebManagementController {

    /**
     * get webid using auth id
     * get specific id using web id
     * set using specific id
     */

    private final WebManagementService webManagementService;
    private final RawStringUtil rawStringUtil;

    public WebManagementController(WebManagementService webManagementService, RawStringUtil rawStringUtil) {
        this.webManagementService = webManagementService;
        this.rawStringUtil = rawStringUtil;
    }

    @Operation(description = "Get manage")
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

    @PostMapping(value ="/admin/web-manage/section-list/create")
    public ResponseEntity<?> createUserSectionList(@Valid @RequestBody WebCreateSectionRequest webCreateSectionRequest){
        WebCreateSectionResponse webCreateSectionResponse = webManagementService.createSectionList(webCreateSectionRequest);
        return ResponseEntity.ok(webCreateSectionResponse);
    }

    @PostMapping(value ="/admin/web-manage/section-list/update")
    public ResponseEntity<?> updateUserSectionList(@Valid @RequestBody WebUpdateSectionRequest webUpdateSectionRequest){
        WebUpdateSectionResponse webUpdateSectionResponse = webManagementService.updateSectionList(webUpdateSectionRequest);
        return ResponseEntity.ok(webUpdateSectionResponse);
    }

    @GetMapping(value ="/public/web-manage/section-list/{authId}")
    public ResponseEntity<?> getUserSectionListByAuthId( @PathVariable("authId") String authId ){
        WebUpdateSectionResponse webUpdateSectionResponse = webManagementService.getUserSectionListById(authId);
        return ResponseEntity.ok(webUpdateSectionResponse);
    }

    @PostMapping(value ="/public/web-manage/uuid/{uuid}")
    public ResponseEntity<?> convertUUID(@PathVariable("uuid") String uuid){

        String validUuid = rawStringUtil.convertToValidUUID(uuid);
        return ResponseEntity.ok(validUuid);
    }

}
