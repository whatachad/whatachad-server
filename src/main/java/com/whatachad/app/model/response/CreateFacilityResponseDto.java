package com.whatachad.app.model.response;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.type.FacilityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateFacilityResponseDto {

    private Address address;
    private FacilityType category;
    private String description;
}
