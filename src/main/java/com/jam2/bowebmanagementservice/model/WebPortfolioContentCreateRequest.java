package com.jam2.bowebmanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebPortfolioContentCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String webPortfolioId;
    private String imageUrl;
    private String title;
    private String description;
    private List<SubPortfolioContentCreateRequest> subPortfolioContents;
}
