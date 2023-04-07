package com.whatachad.app.model.request;

import com.whatachad.app.type.FacilityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateFacilityRequestDto {

    @NotNull
    private Long id;

    private String roadAddress;

    @NotBlank
    private String jibunAddress;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private FacilityType category;

    private String title;

    private String description;
}
