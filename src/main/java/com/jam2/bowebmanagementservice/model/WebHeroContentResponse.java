package com.jam2.bowebmanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebHeroContentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String imageUrl;
    private String title;
    private String description;
}
