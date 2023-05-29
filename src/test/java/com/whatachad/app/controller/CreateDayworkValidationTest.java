package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whatachad.app.common.BError;
import com.whatachad.app.controller.exception.ErrorCode;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
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
public class CreateDayworkValidationTest {

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
    @DisplayName("일정 등록 시 제목이 30자 이상 입력될 수 없다.")
    void daywork_title_length_is_limited_to_30() throws Exception {
        CreateDayworkRequestDto requestDto = CreateDayworkRequestDto.builder()
                .title("장보기,밥먹기,학교가기,티비보기,공부하기,드라마보기,장보기,밥먹기,학교가기,티비보기,공부하기,드라마보기")
                .priority(DayworkPriority.FIRST)
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/dayworks/01",
                                String.format("%d%02d", YEAR, MONTH))
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
    @DisplayName("일정 등록 시 제목이 입력되지 않거나 blank만 입력될 수 없다.")
    void daywork_title_must_be_required() throws Exception {
        CreateDayworkRequestDto requestDto_null = CreateDayworkRequestDto.builder()
                .priority(DayworkPriority.FIRST)
                .build();
        CreateDayworkRequestDto requestDto_blank = CreateDayworkRequestDto.builder()
                .title(" ")
                .priority(DayworkPriority.FIRST)
                .build();

        String request_null = mapper.writeValueAsString(requestDto_null);
        String request_blank = mapper.writeValueAsString(requestDto_blank);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/dayworks/01",
                                String.format("%d%02d", YEAR, MONTH))
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

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/dayworks/01",
                                String.format("%d%02d", YEAR, MONTH))
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
    @DisplayName("일정등록 시 일정의 우선순위가 반드시 입력되어야 한다.")
    void daywork_priority_must_be_required() throws Exception {
        CreateDayworkRequestDto requestDto = CreateDayworkRequestDto.builder()
                .title("밥먹기")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/dayworks/01",
                                String.format("%d%02d", YEAR, MONTH))
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
    @DisplayName("일정 등록 시 우선순위 값은 정의되지 않은 값이 올 수 없다.")
    void daywork_priority_must_be_defined_value() throws Exception {
        BadDayworkDto requestDto = new BadDayworkDto();
        requestDto.setTitle("밥먹기");
        requestDto.setPriority("first");

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/dayworks/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TYPE_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andDo(print());
    }

    @Test
    @DisplayName("일정 등록 시 URL의 yearAndMonth이 {YYYYMM} 형태로 요청되어야 한다.")
    void year_and_month_must_be_pattern_of_YYYYMM() throws Exception {
        CreateDayworkRequestDto requestDto = CreateDayworkRequestDto.builder()
                .title("밥먹기")
                .priority(DayworkPriority.FIRST)
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/dayworks/01",
                                String.format("%d%03d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("registerDaywork.yearAndMonth"))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("pattern.year_and_month", null, null)))
                .andDo(print());
    }

    @Test
    @DisplayName("일정 등록 시 URL의 day는 해당 월에 존재하지 않으면 Buisiness Error이 발생한다.")
    void day_must_be_valid_value() throws Exception {
        CreateDayworkRequestDto requestDto = CreateDayworkRequestDto.builder()
                .title("밥먹기")
                .priority(DayworkPriority.FIRST)
                .build();
        int possibleDay = 35;

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/dayworks/{day}",
                                String.format("%d%02d", YEAR, MONTH), possibleDay)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.BUSINESS_ERROR.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors[0].reason").value(BError.NOT_VALID.getMessage("day")))
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
