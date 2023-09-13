package com.whatachad.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whatachad.app.common.BError;
import com.whatachad.app.controller.exception.ErrorCode;
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
    private MessageSource messageSource;
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
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("account.title.blank", null, null)));
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
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].value").value("제목입니다".repeat(11)))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("account.title.size", null, null)));
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
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("type"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("account.type.null", null, null)));
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
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("category"))
                .andExpect(jsonPath("$.errors[0].value").value(""))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("account.category.blank", null, null)));
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
                .andExpect(jsonPath("$.message").value(ErrorCode.BUSINESS_ERROR.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.BUSINESS_ERROR.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("Business Error"))
                .andExpect(jsonPath("$.errors[0].value").value(BError.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.errors[0].reason").value(BError.NOT_VALID.getMessage("account category")));
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
                .andExpect(jsonPath("$.message").value(ErrorCode.BUSINESS_ERROR.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.BUSINESS_ERROR.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("Business Error"))
                .andExpect(jsonPath("$.errors[0].value").value(BError.NOT_MATCHES.getCode()))
                .andExpect(jsonPath("$.errors[0].reason").value(BError.NOT_MATCHES.getMessage("account type", "account category")));
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
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("cost"))
                .andExpect(jsonPath("$.errors[0].value").value(100000000))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("account.cost.range", null, null)));
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
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()))
                .andExpect(jsonPath("$.errors[0].value").value(String.format("%d-%02d", YEAR, MONTH)))
                .andExpect(jsonPath("$.errors[0].reason").value(messageSource.getMessage("account.year_and_month.pattern", null, null)));
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
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/schedule/{YYYYMM}/accounts/{DD}",
                                String.format("%d%02d", YEAR, MONTH), "not-integer")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_TYPE_VALUE.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_TYPE_VALUE.getCode()))
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
                .andExpect(jsonPath("$.message").value(ErrorCode.BUSINESS_ERROR.getMessage()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.BUSINESS_ERROR.getCode()))
                .andExpect(jsonPath("$.errors[0].field").value("Business Error"))
                .andExpect(jsonPath("$.errors[0].value").value(BError.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.errors[0].reason").value(BError.NOT_VALID.getMessage("day")));
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
