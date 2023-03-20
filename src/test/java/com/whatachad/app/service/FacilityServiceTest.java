package com.whatachad.app.service;

import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityDto;
import com.whatachad.app.model.request.UpdateFacilityDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.type.FacilityType;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class FacilityServiceTest {

    @Autowired
    private FacilityService facilityService;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void initFacility() throws Exception {
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

        CreateFacilityDto createFacilityDto = CreateFacilityDto.builder()
                .jibunAddress("지번주소")
                .lat("0.0")
                .lng("0.0")
                .category(FacilityType.HEALTH)
                .build();
        facilityService.createFacility(createFacilityDto);
    }

    @Test
    @DisplayName("Update Dto를 통해 facility 정보를 수정한다.")
    void updateFacility() {
        Facility facility = facilityService.findAllFacilities().get(0);
        UpdateFacilityDto dto = UpdateFacilityDto.builder()
                .jibunAddress("변경된 주소")
                .lat("0.0")
                .lng("0.0")
                .category(FacilityType.HEALTH)
                .build();
        facilityService.updateFacility(facility.getId(), dto);

        Facility updateFacility = facilityService.findFacility(facility.getId());
        assertThat(updateFacility.getAddress().getJibunAddress()).isEqualTo("변경된 주소");
    }

    @Test
    @DisplayName("facility를 등록할 때 facility 엔티티와 유저 엔티티의 연관 관계가 맺어진다.")
    void createFacilityWithLoginUser() {
        Facility facility = facilityService.findAllFacilities().get(0);
        assertThat(facility.getUser().getId()).isEqualTo("admin");
    }

}