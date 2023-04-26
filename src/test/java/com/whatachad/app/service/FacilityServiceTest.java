package com.whatachad.app.service;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.FindFacilityDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.type.FacilityType;
import com.whatachad.app.util.TestDataProcessor;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FacilityServiceTest {

    @Autowired
    private FacilityService facilityService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TestDataProcessor processor;

    @BeforeEach
    void initFacility() throws Exception {
        authorize();

        FacilityDto facilityDto = FacilityDto.builder()
                .address(Address.builder()
                        .jibunAddress("지번 주소")
                        .latitude(0D)
                        .longitude(0D)
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facilityService.createFacility(facilityDto);
    }

    @AfterEach
    void rollback() {
        processor.rollback();
    }

    @Test
    @DisplayName("Update Dto를 통해 facility 정보를 수정한다.")
    void updateFacility() {
        Facility facility = facilityService.findAllFacilities().get(0);
        FacilityDto updateDto = FacilityDto.builder()
                .id(facility.getId())
                .address(Address.builder()
                        .jibunAddress("변경된 주소")
                        .latitude(0D)
                        .longitude(0D)
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facilityService.updateFacility(updateDto);

        Facility updateFacility = facilityService.findFacility(facility.getId());
        assertThat(updateFacility.getAddress().getJibunAddress()).isEqualTo("변경된 주소");
    }

    @Test
    @DisplayName("facility를 등록할 때 facility 엔티티와 유저 엔티티의 연관 관계가 맺어진다.")
    void createFacilityWithLoginUser() {
        Facility facility = facilityService.findAllFacilities().get(0);
        assertThat(facility.getUser().getId()).isEqualTo("admin");
    }

    @Test
    @DisplayName("유저의 현재 위치로부터 반경 100 떨어진 모든 facility를 조회한다.")
    void findFacilitiesAroundUser() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        FindFacilityDto findFacilityDto = FindFacilityDto.builder()
                .category(FacilityType.HEALTH)
                .latitude(37.484231) // TODO : 테스트 데이터 상수 처리
                .longitude(126.929699)
                .distance(100)
                .build();
        Slice<Facility> facilities = facilityService.findFacilities(pageRequest, findFacilityDto);
        List<Facility> result = facilities.getContent();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("지역명으로 모든 facility를 조회한다.")
    void findFacilitiesByAreaName() {
        FacilityDto facilityDto = FacilityDto.builder()
                .address(Address.builder()
                        .jibunAddress("서울특별시 강남구 청담동 92-22")
                        .latitude(0D)
                        .longitude(0D)
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facilityService.createFacility(facilityDto);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Slice<Facility> facilities = facilityService.findFacilities(pageRequest, "서울특별시 강남구");
        Facility facility = facilities.getContent().get(0);
        assertThat(facility.getAddress().getJibunAddress()).isEqualTo("서울특별시 강남구 청담동 92-22");
        assertThat(facility.getCategory()).isEqualTo(FacilityType.HEALTH);
    }

    private void authorize() {
        UserLoginRequestDto loginDto = UserLoginRequestDto.builder()
                .id("admin")
                .password("admin")
                .build();
        UserTokenResponseDto token = UserTokenResponseDto.builder()
                .accessToken(tokenService.genAccessToken(loginDto.getId()))
                .refreshToken(tokenService.genRefreshToken(loginDto.getId()))
                .build();
        Claims claims = tokenService.validateToken(token.getAccessToken());
        List<String> authorities = (List) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}