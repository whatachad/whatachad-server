package com.whatachad.app.service;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.CreateFacilityResponseDto;
import com.whatachad.app.model.response.FacilityResponseDto;
import com.whatachad.app.model.response.UpdateFacilityResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FacilityMapperService {

    private final FacilityMapService facilityMapService;

    // TODO : 아래 코드를 추상화할 해결책은?

    public FacilityDto toFacilityDto(CreateFacilityRequestDto dto) {
        return FacilityDto.builder()
                .address(createAddress(dto))
                .category(dto.getCategory())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    public FacilityDto toFacilityDto(UpdateFacilityRequestDto dto) {
        return FacilityDto.builder()
                .id(dto.getId())
                .address(createAddress(dto))
                .category(dto.getCategory())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    public CreateFacilityResponseDto toCreateResponseDto(Facility entity) {
        return CreateFacilityResponseDto.builder()
                .address(entity.getAddress())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }

    public FacilityResponseDto toResponseDto(Facility entity) {
        return FacilityResponseDto.builder()
                .id(entity.getId())
                .address(entity.getAddress())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }

    public UpdateFacilityResponseDto toUpdateResponseDto(Facility entity) {
        return UpdateFacilityResponseDto.builder()
                .address(entity.getAddress())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }

    private Address createAddress(CreateFacilityRequestDto dto) {
        return Address.builder()
                .regionCode(facilityMapService
                        .getRegionCode(dto.getJibunAddress()))
                .roadAddress(dto.getRoadAddress())
                .jibunAddress(dto.getJibunAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    private Address createAddress(UpdateFacilityRequestDto dto) {
        return Address.builder()
                .regionCode(facilityMapService
                        .getRegionCode(dto.getJibunAddress()))
                .roadAddress(dto.getRoadAddress())
                .jibunAddress(dto.getJibunAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

}