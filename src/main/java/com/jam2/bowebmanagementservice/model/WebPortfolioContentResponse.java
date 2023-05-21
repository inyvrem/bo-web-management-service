package com.jam2.bowebmanagementservice.model;

import com.jam2.bowebmanagementservice.entity.SubPortfolioContent;
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
public class WebPortfolioContentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String imageUrl;
    private String title;
    private String description;
    private List<SubPortfolioContent> subPortfolioContents;
}
