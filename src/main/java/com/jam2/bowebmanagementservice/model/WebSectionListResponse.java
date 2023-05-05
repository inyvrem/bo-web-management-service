package com.jam2.bowebmanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebSectionListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String authId;
    private String username;
    private String webHeroContent;
    private String webAboutContent;
    private String webPortfolioContent;
    private String webServiceContent;
}
