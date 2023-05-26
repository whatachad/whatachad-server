package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whatachad.app.controller.exception.ErrorCode;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
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
    @DisplayName("일정 수정 시 제목은 30자 이상 입력이 불가능하다.")
    void daywork_title_length_is_limited_to_30() throws Exception {
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
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("Length.daywork.title", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("일정 수정 시 제목이 입력되지 않거나, blank가 입력될 수 없다.")
    void daywork_title_must_be_required() throws Exception {
        UpdateDayworkRequestDto requestDto_null = UpdateDayworkRequestDto.builder()
                .priority(DayworkPriority.FIRST)
                .status(Workcheck.NOT_COMPLETE)
                .build();
        UpdateDayworkRequestDto requestDto_blank = UpdateDayworkRequestDto.builder()
                .title(" ")
                .priority(DayworkPriority.FIRST)
                .status(Workcheck.NOT_COMPLETE)
                .build();

        String request_null = mapper.writeValueAsString(requestDto_null);
        String request_blank = mapper.writeValueAsString(requestDto_blank);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/schedule/{YYYYMM}/dayworks/01/{dayworkId}",
                                String.format("%d%02d", YEAR, MONTH), daywork.getId())
                        .content(request_null)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("NotBlank.daywork.title", null, null)))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/schedule/{YYYYMM}/dayworks/01/{dayworkId}",
                                String.format("%d%02d", YEAR, MONTH), daywork.getId())
                        .content(request_blank)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("NotBlank.daywork.title", null, null)))
                .andDo(print());

    }

    @Test
    @DisplayName("일정 수정 시 일정의 우선순위가 반드시 입력되어야 한다.")
    void daywork_priority_must_be_required() throws Exception {
        UpdateDayworkRequestDto requestDto = UpdateDayworkRequestDto.builder()
                .title("밥먹기")
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
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("priority"))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("NotNull.daywork.priority", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("일정을 생성할 때 정해놓은 우선순위에 포함되지 않은 값이 올 수 없다.")
    void daywork_priority_must_be_defined_value() throws Exception {
        String badPriority = "first";
        BadDayworkDto requestDto = new BadDayworkDto("밥먹기", badPriority, "NOT_COMPLETE");

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/schedule/{YYYYMM}/dayworks/01/{dayworkId}",
                                String.format("%d%02d", YEAR, MONTH), daywork.getId())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TYPE_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andDo(print());
    }

    private static class BadDayworkDto {
        private String title;
        private String priority;
        private String status;

        public BadDayworkDto(String title, String priority, String status) {
            this.title = title;
            this.priority = priority;
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public String getPriority() {
            return priority;
        }

        public String getStatus() {
            return status;
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
