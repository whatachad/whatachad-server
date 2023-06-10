package com.whatachad.app.service;

import com.whatachad.app.init.TestInit;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.request.FindFacilityDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.model.vo.Address;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FacilityServiceTest {

    private static final String ADDRESS_EXAMPLE1 = "서울특별시 중구 태평로1가 31";
    private static final String ROAD_ADDRESS_EXAMPLE = "서울특별시 중구 세종대로 110 서울특별시청";
    private static final double LATITUDE_EXAMPLE1 = 37.5666612;
    private static final double LONGITUDE_EXAMPLE1 = 126.9783785;

    private static final String ADDRESS_EXAMPLE2 = "서울특별시 중구 소공동 1";
    private static final double LATITUDE_EXAMPLE2 = 37.5647103;
    private static final double LONGITUDE_EXAMPLE2 = 126.9816722;

    private Facility facility;

    @Autowired
    private FacilityService facilityService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TestDataProcessor processor;


    @BeforeEach
    void initFacility() {
        authorize();

        FacilityDto facilityDto = FacilityDto.builder()
                .address(Address.builder()
                        .jibunAddress(ADDRESS_EXAMPLE1)
                        .roadAddress(ROAD_ADDRESS_EXAMPLE)
                        .latitude(LATITUDE_EXAMPLE1)
                        .longitude(LONGITUDE_EXAMPLE1)
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facility = facilityService.createFacility(facilityDto);
    }

    @AfterEach
    void rollback() {
        processor.rollback();
    }

    @Test
    @DisplayName("Update Dto를 통해 facility 정보를 수정한다.")
    void updateFacility() {
        Facility findFacility = facilityService.findFacility(facility.getId());
        FacilityDto updateDto = FacilityDto.builder()
                .id(findFacility.getId())
                .address(Address.builder()
                        .jibunAddress(ADDRESS_EXAMPLE2)
                        .latitude(LATITUDE_EXAMPLE2)
                        .longitude(LONGITUDE_EXAMPLE2)
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facilityService.updateFacility(updateDto);

        Facility updateFacility = facilityService.findFacility(findFacility.getId());
        assertThat(updateFacility.getAddress().getJibunAddress()).isEqualTo(ADDRESS_EXAMPLE2);
    }

    @Test
    @DisplayName("Update Dto에 null 값이 포함되더라도 기존 필드 값에는 반영되지 않아야 한다. roadAddress=null, category=null")
    void updateFacilityWithNullValue() {
        Facility findFacility = facilityService.findFacility(facility.getId());
        FacilityDto updateDto = FacilityDto.builder()
                .id(findFacility.getId())
                .address(Address.builder()
                        .jibunAddress(ADDRESS_EXAMPLE2)
                        .latitude(LATITUDE_EXAMPLE2)
                        .longitude(LONGITUDE_EXAMPLE2)
                        .build())
                .build();
        facilityService.updateFacility(updateDto);

        Facility updateFacility = facilityService.findFacility(findFacility.getId());
        assertThat(updateFacility.getAddress().getJibunAddress()).isEqualTo(ADDRESS_EXAMPLE2);
        assertThat(updateFacility.getAddress().getRoadAddress()).isEqualTo(ROAD_ADDRESS_EXAMPLE);
        assertThat(updateFacility.getCategory()).isEqualTo(FacilityType.HEALTH);
    }

    @Test
    @DisplayName("facility를 등록할 때 facility 엔티티와 유저 엔티티의 연관 관계가 맺어진다.")
    void createFacilityWithLoginUser() {
        Facility facility = facilityService.findAllFacilities().get(0);
        assertThat(facility.getUser().getId()).isEqualTo("admin");
    }

    @Test
    @DisplayName("유저의 현재 위치로부터 반경 100m 떨어진 모든 facility를 조회한다.")
    void findFacilitiesAroundUser() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        FindFacilityDto findFacilityDto = FindFacilityDto.builder()
                .category(FacilityType.HEALTH)
                .latitude(TestInit.USER_COORDINATE[0])
                .longitude(TestInit.USER_COORDINATE[1])
                .distance(100)
                .build();
        Slice<Facility> facilities = facilityService.findFacilitiesAroundV1(pageRequest, findFacilityDto);
        List<Facility> result = facilities.getContent();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("지역명으로 모든 facility를 조회한다.")
    void findFacilitiesByAreaName() {
        String[] splitAddress = ADDRESS_EXAMPLE1.split(" ");
        String regionName = Arrays.stream(splitAddress)
                .limit(2)
                .collect(Collectors.joining(" "));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Slice<Facility> facilities = facilityService.findFacilitiesInArea(pageRequest, regionName);
        Facility facility = facilities.getContent().get(0);
        assertThat(facility.getAddress().getJibunAddress()).isEqualTo(ADDRESS_EXAMPLE1);
        assertThat(facility.getCategory()).isEqualTo(FacilityType.HEALTH);
    }

    @Test
    @DisplayName("사용자 주위의 스포츠 시설을 지역코드를 이용해 조회한다.")
    void find_facilities_around_user_by_region_code() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        String[] regionCodes = {"1162010200", "1162010100", "1162010300"};
        Slice<Facility> facilities = facilityService.findFacilitiesAroundV2(pageRequest, FacilityType.HEALTH, regionCodes);
        List<Facility> result = facilities.getContent();
        assertThat(result.size()).isEqualTo(7);
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