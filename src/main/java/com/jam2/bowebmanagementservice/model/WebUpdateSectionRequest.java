package com.jam2.bowebmanagementservice.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUpdateSectionRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = " Id should not be blank")
    private String userWebId;

    @NotNull(message = " isWebHeroContentNeeded should not be blank")
    private Boolean isWebHeroContentNeeded;

    @NotNull(message = " isWebAboutContentNeeded should not be blank")
    private Boolean isWebAboutContentNeeded;

    @NotNull(message = " isWebPortfolioContentNeeded should not be blank")
    private Boolean isWebPortfolioContentNeeded;

    @NotNull(message = " isWebServiceContentNeeded should not be blank")
    private Boolean isWebServiceContentNeeded;
}
