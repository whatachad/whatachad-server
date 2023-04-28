package com.whatachad.app.model.dto;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.type.FacilityType;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class FacilityDto {

    private Long id;
    private Address address;
    private FacilityType category;
    private String title;
    private String description;

}
