package com.whatachad.app.model.request;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.type.FacilityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FacilityDto {

    private Long id;
    private Address address;
    private FacilityType category;
    private String description;

}
