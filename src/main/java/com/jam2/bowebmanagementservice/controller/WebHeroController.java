package com.jam2.bowebmanagementservice.controller;

import com.jam2.bowebmanagementservice.service.WebHeroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="back-office")
public class WebHeroController {
    // save image in cdn

    private final WebHeroService webHeroService;

    public WebHeroController(WebHeroService webHeroService) {
        this.webHeroService = webHeroService;
    }


}
