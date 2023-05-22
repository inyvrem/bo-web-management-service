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
public class SubPortfolioContentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String subPortfolioId;
    private String imageUrl;
    private String title;
    private String description;
}
