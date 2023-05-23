package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.ScheduleService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateAccountValidationTest {

    private static final int YEAR = LocalDate.now().getYear();
    private static final int MONTH = LocalDate.now().getMonthValue();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private TestDataProcessor processor;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private String accessToken;
    private Account account;

    @BeforeEach
    void init() {
        authorize();

        AccountDto accountDto = AccountDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category(AccountCategory.FOOD)
                .build();
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(YEAR)
                .month(MONTH)
                .budget(500000)
                .build();
        account = scheduleService.createAccountOnSchedule(1, accountDto, scheduleDto);
    }

    @AfterEach
    void rollback() {
        processor.rollback();
    }

    @Test
    @DisplayName("가계부 생성 시 제목은 반드시 입력해야 한다.")
    void title_must_be_required() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .cost(10000)
                .type(AccountType.SPEND)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value("제목은 반드시 입력하여야 합니다."));
    }

    @Test
    @DisplayName("가계부 생성 시 제목은 1~50자 이내만 입력 가능하다.")
    void title_must_be_between_1_and_50_characters() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("제목입니다".repeat(11))
                .cost(10000)
                .type(AccountType.SPEND)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].value").value("제목입니다".repeat(11)))
                .andExpect(jsonPath("$.errors[0].reason").value("제목은 1~50자 이내 입력 가능합니다."));
    }


    @Test
    @DisplayName("가계부 생성 시 type은 반드시 입력해야 한다.")
    void account_type_must_be_required() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(10000)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("type"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value("수입/지출 중 하나를 선택해야 합니다."));
    }

    @Test
    @DisplayName("가계부 생성 시 category는 반드시 입력해야 한다.")
    void account_category_must_be_required() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("category"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value("가계부 카테고리를 선택해야 합니다."));
    }

    @Test
    @DisplayName("가계부 생성 시 입력 받은 category가 존재하지 않으면 Business Error가 발생한다.")
    void account_category_must_be_valid() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category("존재하지 않는 카테고리")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Business Error"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("B000"))
                .andExpect(jsonPath("$.errors[0].field").value("Business Error"))
                .andExpect(jsonPath("$.errors[0].value").value("NOT_VALID"))
                .andExpect(jsonPath("$.errors[0].reason").value("category is not valid."));
    }

    @Test
    @DisplayName("가계부 생성 시 입력 받은 category가 SPEND 혹은 INCOME에 대한 category가 아니면 Business Error가 발생한다.")
    void account_category_must_match_with_type_of_that() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.INCOME)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Business Error"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("B000"))
                .andExpect(jsonPath("$.errors[0].field").value("Business Error"))
                .andExpect(jsonPath("$.errors[0].value").value("NOT_MATCHES"))
                .andExpect(jsonPath("$.errors[0].reason").value("account type and account category do not match."));
    }

    @Test
    @DisplayName("가계부 생성 시 cost는 0~99999999까지만 입력 가능하다.")
    void account_cost_must_be_between_0_and_99999999() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(100000000)
                .type(AccountType.SPEND)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("cost"))
                .andExpect(jsonPath("$.errors[0].value").value(100000000))
                .andExpect(jsonPath("$.errors[0].reason").value("범위를 초과합니다."));
    }

    @Test
    @DisplayName("가계부 생성 시 URL의 yearAndMonth {YYYYMM} 형태로 요청해야 한다.")
    void year_and_month_must_be_pattern_of_YYYYMM() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/01",
                                String.format("%d-%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Input Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.errors[0].field").value("registerAccount.yearAndMonth"))
                .andExpect(jsonPath("$.errors[0].value").value(String.format("%d-%02d", YEAR, MONTH)))
                .andExpect(jsonPath("$.errors[0].reason").value("{YYYYMM}과 같은 형태로 요청해야 합니다."));
    }

    @Test
    @DisplayName("가계부 생성 시 URL의 day는 int type으로 변환될 수 있어야 한다.")
    void day_must_be_converted_to_integer() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/not-integer",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Type Value"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C005"))
                .andExpect(jsonPath("$.errors[0].field").value("DD"))
                .andExpect(jsonPath("$.errors[0].value").value("not-integer"))
                .andExpect(jsonPath("$.errors[0].reason").value("typeMismatch"));
    }

    @Test
    @DisplayName("가계부 생성 시 URL의 day는 해당 월에서 존재하지 않으면 Business Errorr가 발생한다.")
    void day_must_be_valid_value() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/32",
                                String.format("%d%02d", YEAR, MONTH))
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Business Error"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("B000"))
                .andExpect(jsonPath("$.errors[0].field").value("Business Error"))
                .andExpect(jsonPath("$.errors[0].value").value("NOT_VALID"))
                .andExpect(jsonPath("$.errors[0].reason").value("day is not valid."));
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
