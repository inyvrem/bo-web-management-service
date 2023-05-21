package com.jam2.bowebmanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUpdateSectionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String authId;
    private String username;
    private HashMap<String,Object> sectionList;
}
