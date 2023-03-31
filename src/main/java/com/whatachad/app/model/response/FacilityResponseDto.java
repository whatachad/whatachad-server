package com.whatachad.app.model.response;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.type.FacilityType;
import lombok.*;

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

    public FacilityResponseDto(Facility facility) {
        this.id = facility.getId();
        this.address = facility.getAddress();
        this.category = facility.getCategory();
        this.title = facility.getTitle();
        this.description = facility.getDescription();
    }
}
