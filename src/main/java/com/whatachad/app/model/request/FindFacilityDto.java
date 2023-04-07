package com.whatachad.app.model.request;

import com.whatachad.app.type.FacilityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FindFacilityDto {

    private FacilityType category;
    private Double latitude;
    private Double longitude;
    private Integer distance;

}
