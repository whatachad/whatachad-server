package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.DayworkService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
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
    private Integer year, month, date;
    Daywork daywork;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DayworkService dayworkService;
    @Autowired
    private TokenService tokenService;


    @BeforeEach
    void init() {
        authorize();
        year = 2023; month = 10; date = 5;

        DateTime dateTime = DateTime.builder()
                .date(date)
                .hour(10)
                .minute(10)
                .build();

        DayworkDto dayworkDto = DayworkDto.builder()
                .title("title")
                .priority(DayworkPriority.FIRST)
                .dateTime(dateTime)
                .status(Workcheck.NOT_COMPLETE)
                .build();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(year)
                .month(month)
                .budget(1000)
                .build();

        //daywork = dayworkService.createDaywork(dayworkDto, scheduleDto);
    }
    @Test
    @DisplayName("daywork를 수정한다. Put /v1/schedules/{YYYYMM}/dayworks/{DD}/{daywork_id}")
    public void editDaywork() throws Exception{
        //given
        String changeTitle = "변경이름";
        UpdateDayworkRequestDto updateDayworkDto = UpdateDayworkRequestDto.builder()
                .title(changeTitle)
                .hour(5)
                .minute(5)
                .status(Workcheck.COMPLETE)
                .priority(DayworkPriority.SECOND)
                .build();

        String request = mapper.writeValueAsString(updateDayworkDto);

        //when, then
        Long dayworkId = daywork.getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/schedules/"
                                + year + month
                                + "/dayworks/"
                                + date + "/"
                                + dayworkId)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(changeTitle));
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
