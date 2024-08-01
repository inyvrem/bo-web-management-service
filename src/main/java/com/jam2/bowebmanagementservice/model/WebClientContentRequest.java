package com.jam2.bowebmanagementservice.model;

import com.jam2.bowebmanagementservice.entity.SubClientContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebClientContentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String webClientId;
    private String imageUrl;
    private String title;
    private String description;
    private List<SubClientContent> subClientContents;
}
