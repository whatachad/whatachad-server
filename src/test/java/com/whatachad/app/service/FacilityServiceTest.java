package com.whatachad.app.service;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.dto.CreateFacilityDto;
import com.whatachad.app.model.dto.UpdateFacilityDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FacilityServiceTest {

    @Autowired
    private FacilityService facilityService;

    @BeforeEach
    void initFacility() {
        CreateFacilityDto dto = CreateFacilityDto.builder()
                .address(new Address("도로주소", "지번주소", "지역코드", "위도", "경도"))
                .build();
        facilityService.createFacility(dto);
    }

    @Test
    @DisplayName("Update Dto를 통해 facility 정보를 수정한다.")
    void updateFacility() {
        Facility facility = facilityService.findAllFacilities().get(0);
        UpdateFacilityDto dto = UpdateFacilityDto.builder()
                .address(new Address("변경된 도로주소", "지번주소", "지역코드", "위도", "경도"))
                .build();
        facilityService.updateFacility(facility.getId(), dto);

        Facility updateFacility = facilityService.findFacility(facility.getId());
        assertThat(updateFacility.getAddress().getRoadAddress()).isEqualTo("변경된 도로주소");
    }

}