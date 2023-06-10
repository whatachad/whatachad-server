package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatachad.app.init.TestInit;
import com.whatachad.app.model.vo.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.FacilityService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.type.FacilityType;
import com.whatachad.app.util.TestDataProcessor;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FacilityControllerTest {

    private static final String ADDRESS_EXAMPLE1 = "서울특별시 중구 태평로1가 31";
    private static final String ROAD_ADDRESS_EXAMPLE = "서울특별시 중구 세종대로 110 서울특별시청";
    private static final double LATITUDE_EXAMPLE1 = 37.5666612;
    private static final double LONGITUDE_EXAMPLE1 = 126.9783785;

    private static final String ADDRESS_EXAMPLE2 = "서울특별시 중구 소공동 1";
    private static final double LATITUDE_EXAMPLE2 = 37.5647103;
    private static final double LONGITUDE_EXAMPLE2 = 126.9816722;

    private final ObjectMapper mapper = new ObjectMapper();
    private Facility facility;
    private String accessToken;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TestDataProcessor processor;

    @BeforeEach
    void init() {
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
    @DisplayName("facility를 등록한다. POST /v1/facilities")
    void registerFacility() throws Exception {
        CreateFacilityRequestDto createFacilityDto = CreateFacilityRequestDto.builder()
                .jibunAddress(ADDRESS_EXAMPLE2)
                .latitude(LATITUDE_EXAMPLE2)
                .longitude(LONGITUDE_EXAMPLE2)
                .category(FacilityType.HEALTH)
                .build();
        String request = mapper.writeValueAsString(createFacilityDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/facilities")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value(ADDRESS_EXAMPLE2))
                .andExpect(jsonPath("$.address.latitude").value(String.valueOf(LATITUDE_EXAMPLE2)))
                .andExpect(jsonPath("$.address.longitude").value(String.valueOf(LONGITUDE_EXAMPLE2)))
                .andExpect(jsonPath("$.category").value("HEALTH"));
    }

    @Test
    @DisplayName("facility를 수정한다. PUT /v1/facilities")
    void editFacility() throws Exception {
        UpdateFacilityRequestDto updateFacilityDto = UpdateFacilityRequestDto.builder()
                .id(facility.getId())
                .jibunAddress(ADDRESS_EXAMPLE2)
                .latitude(LATITUDE_EXAMPLE2)
                .longitude(LONGITUDE_EXAMPLE2)
                .category(FacilityType.HEALTH)
                .build();
        String request = mapper.writeValueAsString(updateFacilityDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/facilities")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value(ADDRESS_EXAMPLE2));
    }

    @Test
    @DisplayName("facility를 삭제한다. DELETE /v1/facilities/{facilityId}")
    void deleteFacility() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/facilities/" + facility.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("facility 하나를 조회한다. GET /v1/facilities/{facilityId}")
    void getFacility() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities/" + facility.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value(ADDRESS_EXAMPLE1))
                .andExpect(jsonPath("$.category").value("HEALTH"));
    }

    @Test
    @DisplayName("카테고리와 거리를 지정하고 내 주변 facility를 조회한다. " +
            "GET /v1/facilities?page=0&size=5&category=HEALTH&latitude=37.484231&longitude=126.929699&distance=100")
    void getFacilityAround() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities")
                        .param("page", "0")
                        .param("size", "5")
                        .param("category", "HEALTH")
                        .param("latitude", "37.484231")
                        .param("longitude", "126.929699")
                        .param("distance", "100")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @DisplayName("facility를 잘못된 형식의 카테고리 값으로 조회하면 400 ERROR가 발생한다. " +
            "GET /v1/facilities?page=0&size=5&category=health&latitude=37.484231&longitude=126.929699&distance=100")
    void getFacilityByInvalidCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities")
                        .param("page", "0")
                        .param("size", "5")
                        .param("category", "health")
                        .param("latitude", "37.484231")
                        .param("longitude", "126.929699")
                        .param("distance", "100")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].reason").value("category is not valid."));
    }

    @Test
    @DisplayName("주변 시설을 조회할 때 위도, 경도, 거리 중에서 값이 하나라도 빠지면 400 ERROR가 발생한다." +
            "GET /v1/facilities?page=0&size=5&category=health&latitude=37.484231&longitude=126.929699")
    void getFacilityByEmptyGeometricValue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities")
                        .param("page", "0")
                        .param("size", "5")
                        .param("category", "health")
                        .param("latitude", "37.484231")
                        .param("longitude", "126.929699")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].reason").value("distance does not exist."));
    }

    @Test
    @DisplayName("facility를 지역명으로 조회한다. GET /v1/facilities/search?page=0&size=5&l1=서울특별시&l2=중구")
    void getFacilityByArea() throws Exception {
        String[] splitAddress = ADDRESS_EXAMPLE1.split(" ");
        String l1 = splitAddress[0];
        String l2 = splitAddress[1];
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities/search")
                        .param("l1", l1)
                        .param("l2", l2)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].address.jibunAddress").value(ADDRESS_EXAMPLE1));
    }

    @Test
    @DisplayName("사용자 주변 스포츠 시설을 조회한다. (지역코드 이용하는 버전) " +
            "GET /v1/facilities/v2?page=0&size=5&category=HEALTH&latitude=37.4846553&longitude=126.9272265&distance=1")
    void find_facilities_around_user_v2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities/v2")
                        .param("page", "0")
                        .param("size", "5")
                        .param("category", "HEALTH")
                        .param("latitude", String.valueOf(TestInit.LAT_LNG[0][0]))
                        .param("longitude", String.valueOf(TestInit.LAT_LNG[0][1]))
                        .param("distance", "1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5));
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
        accessToken = token.getAccessToken();
        Claims claims = tokenService.validateToken(accessToken);
        List<String> authorities = (List) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}

