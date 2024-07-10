package com.jam2.bowebmanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebPortfolioContentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID webPortfolioId;
    private String imageUrl;
    private String title;
    private String description;
    private List<SubPortfolioContentResponse> subPortfolioContents;
}
