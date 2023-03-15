package com.whatachad.app.model.dto;

import com.whatachad.app.model.domain.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateFacilityDto {

    @NotEmpty // TODO : validation 로직 필요
    private Address address;
}
