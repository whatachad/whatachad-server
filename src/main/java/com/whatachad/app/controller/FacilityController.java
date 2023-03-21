package com.whatachad.app.controller;

import com.whatachad.app.api.FacilityApi;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.CreateFacilityResponseDto;
import com.whatachad.app.model.response.UpdateFacilityResponseDto;
import com.whatachad.app.service.FacilityService;
import com.whatachad.app.service.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FacilityController implements FacilityApi {

    private final FacilityService facilityService;
    private final MapperService mapperService;

    @Override
    public ResponseEntity<CreateFacilityResponseDto> registerFacility(CreateFacilityRequestDto requestDto) {
        Facility facility = facilityService.createFacility(requestDto);
        return new ResponseEntity<>(mapperService.toCreateResponseDto(facility), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UpdateFacilityResponseDto> editFacility(UpdateFacilityRequestDto requestDto) {
        facilityService.updateFacility(requestDto);
        Facility facility = facilityService.findFacility(requestDto.getId());
        return new ResponseEntity<>(mapperService.toUpdateResponseDto(facility), HttpStatus.OK);
    }

    @Override
    public void deleteFacility(Long facilityId) {
        facilityService.deleteFacility(facilityId);
    }
}
