package com.whatachad.app.model.request;

import com.whatachad.app.type.FacilityType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateFacilityDto {

    private String roadAddress;

    @NotBlank
    private String jibunAddress;

    @NotBlank
    private String lat;

    @NotBlank
    private String lng;

    @NotBlank
    private FacilityType category;

    private String description;
}
