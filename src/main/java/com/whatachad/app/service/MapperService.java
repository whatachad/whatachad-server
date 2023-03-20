package com.whatachad.app.service;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.request.CreateFacilityDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.UpdateFacilityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MapperService {

    private final FacilityMapService facilityMapService;

    public FacilityDto toFacilityDto(CreateFacilityDto dto) {
        return FacilityDto.builder()
                .address(createAddress(dto))
                .category(dto.getCategory())
                .description(dto.getDescription())
                .build();
    }

    public FacilityDto toFacilityDto(UpdateFacilityDto dto) {
        return FacilityDto.builder()
                .address(createAddress(dto))
                .category(dto.getCategory())
                .description(dto.getDescription())
                .build();
    }

    private Address createAddress(CreateFacilityDto dto) {
        return Address.builder()
                .regionCode(facilityMapService
                        .getRegionCode(dto.getJibunAddress()))
                .roadAddress(dto.getRoadAddress())
                .jibunAddress(dto.getJibunAddress())
                .latitude(dto.getLat())
                .longitude(dto.getLng())
                .build();
    }

    private Address createAddress(UpdateFacilityDto dto) {
        return Address.builder()
                .regionCode(facilityMapService
                        .getRegionCode(dto.getJibunAddress()))
                .roadAddress(dto.getRoadAddress())
                .jibunAddress(dto.getJibunAddress())
                .latitude(dto.getLat())
                .longitude(dto.getLng())
                .build();
    }

}