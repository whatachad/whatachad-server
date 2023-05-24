package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whatachad.app.controller.exception.ErrorCode;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.ScheduleService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import com.whatachad.app.util.TestDataProcessor;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateDayworkValidationTest {

    private static final int YEAR = LocalDate.now().getYear();
    private static final int MONTH = LocalDate.now().getMonthValue();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TestDataProcessor processor;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ScheduleService scheduleService;


    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private String accessToken;
    private Daywork daywork;

    @AfterEach
    void rollback() {
        processor.rollback();
    }

    @BeforeEach
    void init() {
        authorize();

        DayworkDto dayworkDto = DayworkDto.builder()
                .title("코딩하기")
                .priority(DayworkPriority.FIRST)
                .status(Workcheck.NOT_COMPLETE)
                .build();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(YEAR)
                .month(MONTH)
                .budget(500000)
                .build();
        daywork = scheduleService.createDayworkOnSchedule(1, dayworkDto, scheduleDto);
    }

    @Test
    @DisplayName("daywork를 수정할 때 title 길이가 30자 이상이면 BAD REQUEST 에러가 발생한다." +
            "PUT /v1/schedule/{YYYYMM}/dayworks/01")
    void validate_update_dayworks_title_length() throws Exception {
        UpdateDayworkRequestDto requestDto = UpdateDayworkRequestDto.builder()
                .title("장보기,밥먹기,학교가기,티비보기,공부하기,드라마보기,장보기,밥먹기,학교가기,티비보기,공부하기,드라마보기")
                .priority(DayworkPriority.FIRST)
                .status(Workcheck.NOT_COMPLETE)
                .build();
        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/schedule/{YYYYMM}/dayworks/01/{dayworkId}",
                                String.format("%d%02d", YEAR, MONTH), daywork.getId())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("Length.daywork.title",null,null)))
                .andDo(print());
    }


    private static class BadDayworkDto{
        private String title;
        private String priority;

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getPriority() {
            return priority;
        }
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
