package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.FindFacilityDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.CreateFacilityResponseDto;
import com.whatachad.app.model.response.FacilityResponseDto;
import com.whatachad.app.model.response.UpdateFacilityResponseDto;
import com.whatachad.app.type.FacilityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    public FindFacilityDto toFindFacilityDto(Map<String, String> findFacilityParam) {
        Map<String, String> validParam = new HashMap<>();
        String[] keys = new String[]{"category", "latitude", "longitude", "distance"};
        Arrays.stream(keys)
                .forEach(k -> {
                    validParam.put(k, null);
                });
        validParam.putAll(findFacilityParam);
        validParam.entrySet()
                .forEach(e -> validate(e));
        return FindFacilityDto.builder()
                .category(FacilityType.valueOf(validParam.get("category")))
                .latitude(Double.valueOf(validParam.get("latitude")))
                .longitude(Double.valueOf(validParam.get("longitude")))
                .distance(Integer.valueOf(validParam.get("distance")))
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

    private void validate(Map.Entry entry) {
        // TODO : 존재하지 않는 카테고리, 잘못된 형식의 위도/경도, 거리 타입 검증 등
        // 카테고리 값이 존재하지 않는다면 "HEALTH"를 default로서 지정한다.
        Object value = entry.getValue();
        if (entry.getKey().equals("category")) {
            if (value == null) {
                entry.setValue("HEALTH");
            } else if (!FacilityType.contains((String) value)) {
                throw new CommonException(BError.NOT_VALID, (String) entry.getKey());
            }
        } else {
            if (value == null) {
                // 다른 파라미터의 값이 존재하지 않는다면 예외를 던진다.
                throw new CommonException(BError.NOT_EXIST, (String) entry.getKey());
            }
        }
    }
}