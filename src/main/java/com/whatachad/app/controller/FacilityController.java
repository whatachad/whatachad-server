package com.whatachad.app.controller;

import com.whatachad.app.api.FacilityApi;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.CreateFacilityResponseDto;
import com.whatachad.app.model.response.FacilityResponseDto;
import com.whatachad.app.model.response.UpdateFacilityResponseDto;
import com.whatachad.app.service.FacilityService;
import com.whatachad.app.service.FacilityMapperService;
import com.whatachad.app.type.FacilityType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FacilityController implements FacilityApi {

    private final FacilityService facilityService;
    private final FacilityMapperService mapperService;

    @Override
    public ResponseEntity<CreateFacilityResponseDto> registerFacility(CreateFacilityRequestDto requestDto) {
        FacilityDto facilityDto = mapperService.toFacilityDto(requestDto);
        Facility facility = facilityService.createFacility(facilityDto);
        return new ResponseEntity<>(mapperService.toCreateResponseDto(facility), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Slice<FacilityResponseDto>> getFacilities(Pageable pageable, FacilityType category) {
        Slice<Facility> facilities = facilityService.findFacilities(pageable, category);
        return ResponseEntity.ok(facilities.map(FacilityResponseDto::new));
    }

    @Override
    public ResponseEntity<FacilityResponseDto> getFacility(Long facilityId) {
        Facility facility = facilityService.findFacility(facilityId);
        return new ResponseEntity<>(mapperService.toResponseDto(facility), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UpdateFacilityResponseDto> editFacility(UpdateFacilityRequestDto requestDto) {
        FacilityDto facilityDto = mapperService.toFacilityDto(requestDto);
        facilityService.updateFacility(facilityDto);
        Facility facility = facilityService.findFacility(requestDto.getId());
        return new ResponseEntity<>(mapperService.toUpdateResponseDto(facility), HttpStatus.OK);
    }

    @Override
    public void deleteFacility(Long facilityId) {
        facilityService.deleteFacility(facilityId);
    }
}
