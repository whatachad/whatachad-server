package com.whatachad.app.service;

import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.FacilityDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FacilityMapperServiceTest {

    @Autowired
    FacilityMapperService mapperService;

    @Test
    @DisplayName("지번 주소가 입력으로 주어질 때 적절한 지역 코드가 반환되어야 한다.")
    void getRegionCode() {
        String address = "서울특별시 강남구 삼성동 159";

        CreateFacilityRequestDto dto = CreateFacilityRequestDto.builder()
                .jibunAddress(address)
                .build();
        FacilityDto facilityDto = mapperService.toFacilityDto(dto);

        assertThat(facilityDto.getAddress().getRegionCode()).isEqualTo("1168010500");
    }

}