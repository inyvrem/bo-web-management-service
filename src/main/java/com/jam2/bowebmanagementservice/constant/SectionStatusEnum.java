package com.jam2.bowebmanagementservice.constant;

public enum SectionStatusEnum {

    ACTIVE(1,"active section"),
    DELETE(2,"delete section"),
    INACTIVE(3,"inactive section");

    private Integer statusCode;
    private String description;

    SectionStatusEnum(Integer statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }

}
