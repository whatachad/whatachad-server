package com.whatachad.app.model.response;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.type.FacilityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FacilityResponseDto {

    private Long id;
    private Address address;
    private FacilityType category;
    private String title;
    private String description;

}
