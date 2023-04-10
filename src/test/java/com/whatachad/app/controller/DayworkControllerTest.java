package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.DayworkService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.type.DayworkPriority;
import io.jsonwebtoken.Claims;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DayworkControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private String accessToken;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DayworkService dayworkService;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void init() {
        authorize();
    }

    @Test
    @DisplayName("daywork를 등록한다. Post /v1/schedules/{YYYYMM}/dayworks/{DD}")
    void registerDaywork() throws Exception {
        String title = "할 일";
        DayworkPriority dayworkPriority = DayworkPriority.FIRST;

        CreateDayworkRequestDto createDayworkRequestDto = CreateDayworkRequestDto.builder()
                .title(title)
                .priority(dayworkPriority)
                .hour(10)
                .minute(5)
                .build();

        String YYYYMM = "202305";
        String DD = "10";

        String request = mapper.writeValueAsString(createDayworkRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedules/" + YYYYMM + "/dayworks/" + DD)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title));
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
