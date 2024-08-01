package com.jam2.bowebmanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubClientContentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String subClientId;
    private String imageUrl;
    private String title;
    private String description;
    private String customerName;
}
