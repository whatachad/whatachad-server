package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.FacilityService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.type.FacilityType;
import io.jsonwebtoken.Claims;
import org.hamcrest.core.IsNull;
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

    private final ObjectMapper mapper = new ObjectMapper();
    private Facility facility;
    private String accessToken;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void init() {
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

        FacilityDto facilityDto = FacilityDto.builder()
                .address(Address.builder()
                        .jibunAddress("지번 주소")
                        .latitude("0.0")
                        .longitude("0.0")
                        .build())
                .category(FacilityType.HEALTH)
                .build();
        facility = facilityService.createFacility(facilityDto);
    }

    @Test
    @DisplayName("facility를 등록한다. POST /v1/facilities")
    void registerFacility() throws Exception {
        CreateFacilityRequestDto createFacilityDto = CreateFacilityRequestDto.builder()
                .jibunAddress("지번 주소")
                .latitude("0.0")
                .longitude("0.0")
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
                .latitude("0.0")
                .longitude("0.0")
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
        UpdateFacilityRequestDto updateFacilityDto = UpdateFacilityRequestDto.builder()
                .id(facility.getId())
                .jibunAddress("변경된 주소")
                .latitude("0.0")
                .longitude("0.0")
                .category(FacilityType.HEALTH)
                .build();
        String request = mapper.writeValueAsString(updateFacilityDto);
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
    @DisplayName("facility를 조회한다. GET /v1/facilities/{facilityId}")
    void getFacility() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities/" + facility.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.jibunAddress").value("지번 주소"))
                .andExpect(jsonPath("$.category").value("HEALTH"))
                .andDo(print());
    }

    // TODO : 예외 상황에 대한 테스트 필요
}