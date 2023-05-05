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
public class WebSectionListRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userWebId;
    private Boolean webHeroContent;
    private Boolean webAboutContent;
    private Boolean webPortfolioContent;
    private Boolean webServiceContent;
}
