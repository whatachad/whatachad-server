package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatachad.app.TestInit;
import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.FacilityResponseDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.FacilityMapperService;
import com.whatachad.app.service.FacilityService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.type.FacilityType;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FacilityControllerTest {

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
    private EntityManager em;
    @Autowired
    private PlatformTransactionManager txManager;

    @BeforeEach
    void init() {
        authorize();
        FacilityDto facilityDto = FacilityDto.builder()
                .address(Address.builder()
                        .jibunAddress("지번 주소")
                        .latitude(0D)
                        .longitude(0D)
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facility = facilityService.createFacility(facilityDto);
    }

    @AfterEach
    void rollback() {
        TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionDefinition());
        em.createQuery("delete from Facility f where f.title not in :title")
                .setParameter("title", List.of(TestInit.FACILITY_TITLE))
                .executeUpdate();
        txManager.commit(txStatus);
    }

    @Test
    @DisplayName("facility를 등록한다. POST /v1/facilities")
    void registerFacility() throws Exception {
        CreateFacilityRequestDto createFacilityDto = CreateFacilityRequestDto.builder()
                .jibunAddress("지번 주소")
                .latitude(0D)
                .longitude(0D)
                .category(FacilityType.HEALTH)
                .build();
        String request = mapper.writeValueAsString(createFacilityDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/facilities")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value("지번 주소"))
                .andExpect(jsonPath("$.address.latitude").value("0.0"))
                .andExpect(jsonPath("$.address.longitude").value("0.0"))
                .andExpect(jsonPath("$.category").value("HEALTH"));
    }

    @Test
    @DisplayName("facility를 수정한다. PUT /v1/facilities")
    void editFacility() throws Exception {
        UpdateFacilityRequestDto updateFacilityDto = UpdateFacilityRequestDto.builder()
                .id(facility.getId())
                .jibunAddress("변경된 주소")
                .latitude(0D)
                .longitude(0D)
                .category(FacilityType.HEALTH)
                .build();
        String request = mapper.writeValueAsString(updateFacilityDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/facilities")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value("변경된 주소"));
    }

    @Test
    @DisplayName("facility를 수정할 때 변경되는 필드 외에 기존 필드 값을 유지하지 않으면 " +
            "변경 후에 null 값이 저장된다. PUT /v1/facilities")
    void editFacilityWithoutExistingField() throws Exception {
        UpdateFacilityRequestDto updateDto = UpdateFacilityRequestDto.builder()
                .id(facility.getId())
                .jibunAddress("변경된 주소")
                .latitude(0D)
                .longitude(0D)
                .category(FacilityType.HEALTH)
                .build();
        String request = mapper.writeValueAsString(updateDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/facilities")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value("변경된 주소"))
                .andExpect(jsonPath("$.address.roadAddress").value(IsNull.nullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("facility를 삭제한다. DELETE /v1/facilities/{facilityId}")
    void deleteFacility() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/facilities/" + facility.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("facility 하나를 조회한다. GET /v1/facilities/{facilityId}")
    void getFacility() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities/" + facility.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value("지번 주소"))
                .andExpect(jsonPath("$.category").value("HEALTH"))
                .andDo(print());
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
                .andExpect(jsonPath("$.content.length()").value(2))
                .andDo(print());
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
                .andExpect(jsonPath("$.errors[0].reason").value("category is not valid."))
                .andDo(print());
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
                .andExpect(jsonPath("$.errors[0].reason").value("distance does not exist."))
                .andDo(print());
    }

    @Test
    @DisplayName("facility를 지역명으로 조회한다. GET /v1/facilities/search?page=0&size=5&l1=서울특별시&l2=강남구&l3=청담동")
    void getFacilityByArea() throws Exception {
        FacilityDto facilityDto = FacilityDto.builder()
                .address(Address.builder()
                        .jibunAddress("서울특별시 강남구 청담동 92-22")
                        .latitude(0D)
                        .longitude(0D)
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facilityService.createFacility(facilityDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities/search")
                        .param("l1", "서울특별시")
                        .param("l2", "강남구")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].address.jibunAddress")
                        .value("서울특별시 강남구 청담동 92-22"))
                .andDo(print());
    }

    // TODO : 예외 상황에 대한 테스트 필요 -> 하나의 기능에 대한 여러 파라미터 값으로 테스트 (Parameterized Test)

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

