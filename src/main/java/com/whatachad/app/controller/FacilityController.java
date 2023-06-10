package com.whatachad.app.controller;

import com.whatachad.app.api.FacilityApi;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.mapper.FacilityConverter;
import com.whatachad.app.model.request.*;
import com.whatachad.app.model.response.FacilityResponseDto;
import com.whatachad.app.service.FacilityService;
import com.whatachad.app.service.LocalMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FacilityController implements FacilityApi {

    private final FacilityService facilityService;
    private final LocalMapService localMapService;
    private final FacilityConverter facilityConverter;

    @Override
    public ResponseEntity<FacilityResponseDto> registerFacility(CreateFacilityRequestDto requestDto) {
        FacilityDto facilityDto = facilityConverter.toFacilityDto(requestDto);
        Facility facility = facilityService.createFacility(facilityDto);
        return ResponseEntity.ok(facilityConverter.toResponseDto(facility));
    }

    @Override
    public ResponseEntity<Slice<FacilityResponseDto>> getFacilitiesAround(Pageable pageable,
                                                                          Map<String, String> findFacilityParam) {
        FindFacilityDto findFacilityDto = facilityConverter.toFindFacilityDto(findFacilityParam);
        Slice<Facility> facilities = facilityService.findFacilitiesAroundV1(pageable, findFacilityDto);
        return ResponseEntity.ok(facilities.map(FacilityResponseDto::new));
    }

    @Override
    public ResponseEntity<Slice<FacilityResponseDto>> getFacilitiesAroundV2(Pageable pageable,
                                                                            Map<String, String> findFacilityParam) {

        FindFacilityDto findFacilityDto = facilityConverter.toFindFacilityDto(findFacilityParam);
        String[] addressesWith4Directions = localMapService
                .findAddressesWith4Directions(
                        findFacilityDto.getLatitude(),
                        findFacilityDto.getLongitude(),
                        findFacilityDto.getDistance()
                );
        String[] regionCodes = Arrays.stream(addressesWith4Directions)
                .map(facilityConverter::getRegionCode)
                .toArray(String[]::new);
        Slice<Facility> facilities = facilityService.findFacilitiesAroundV2(pageable, regionCodes);
        return ResponseEntity.ok(facilities.map(FacilityResponseDto::new));
    }

    @Override
    public ResponseEntity<Slice<FacilityResponseDto>> getFacilitiesBySearchCond(Pageable pageable,
                                                                                String l1, String l2, String l3) {
        Slice<Facility> facilities = facilityService.findFacilitiesInArea(pageable, AreaRequestDto.getArea(l1, l2, l3));
        return ResponseEntity.ok(facilities.map(FacilityResponseDto::new));
    }

    @Override
    public ResponseEntity<FacilityResponseDto> getFacility(Long facilityId) {
        Facility facility = facilityService.findFacility(facilityId);
        return ResponseEntity.ok(facilityConverter.toResponseDto(facility));
    }

    @Override
    public ResponseEntity<FacilityResponseDto> editFacility(UpdateFacilityRequestDto requestDto) {
        FacilityDto facilityDto = facilityConverter.toFacilityDto(requestDto);
        facilityService.updateFacility(facilityDto);
        Facility facility = facilityService.findFacility(requestDto.getId());
        return ResponseEntity.ok(facilityConverter.toResponseDto(facility));
    }

    @Override
    public void deleteFacility(Long facilityId) {
        facilityService.deleteFacility(facilityId);
    }
}
