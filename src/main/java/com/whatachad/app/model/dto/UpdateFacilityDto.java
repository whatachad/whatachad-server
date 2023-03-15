package com.whatachad.app.model.dto;

import com.whatachad.app.model.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateFacilityDto {

    private Address address;
}
