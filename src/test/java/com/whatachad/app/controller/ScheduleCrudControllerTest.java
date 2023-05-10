package com.whatachad.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.DayworksResponseDto;
import com.whatachad.app.model.response.RecentScheduleResponseDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleCrudControllerTest {

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
    @DisplayName("가계부를 등록한다. POST /v1/schedule/202304/accounts/01")
    void registerAccount() throws Exception {
        CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .title("장보기")
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountDate").value(String.format("%d-%02d-%02d", YEAR, MONTH, 1)))
                .andExpect(jsonPath("$.title").value("장보기"))
                .andExpect(jsonPath("$.cost").value(10000))
                .andExpect(jsonPath("$.type").value("SPEND"))
                .andExpect(jsonPath("$.category").value("식비"))
                .andDo(print());
    }

    @Test
    @DisplayName("가계부를 수정한다. PUT /v1/schedule/{YYYYMM}/accounts/01")
    void updateAccount() throws Exception {
        UpdateAccountRequestDto requestDto = UpdateAccountRequestDto.builder()
                .title("장보기")
                .cost(8000)
                .type(AccountType.SPEND)
                .category("식비")
                .build();

        String request = mapper.writeValueAsString(requestDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/schedule/{YYYYMM}/accounts/01/{accountId}",
                                String.format("%d%02d", YEAR, MONTH), account.getId())
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountDate").value(String.format("%d-%02d-%02d", YEAR, MONTH, 1)))
                .andExpect(jsonPath("$.title").value("장보기"))
                .andExpect(jsonPath("$.cost").value(8000))
                .andExpect(jsonPath("$.type").value("SPEND"))
                .andExpect(jsonPath("$.category").value("식비"));
    }

    @Test
    @DisplayName("캘린더 조회 시 일별로 최대 3개의 Daywork가 조회된다. GET /v1/schedule/{YYYYMM}")
    void retrieve_calendar() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/schedule/{YYYYMM}",
                                String.format("%d%02d", YEAR, MONTH))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        List<DayworksResponseDto> response = mapper.readValue(json, new TypeReference<>(){});

        assertThat(response).isSortedAccordingTo((o1, o2) -> {
            if (o1.getDate().isBefore(o2.getDate())) return -1;
            if (o1.getDate().isAfter(o2.getDate())) return 1;
            return 0;
        });
        response.forEach(res -> {
            assertThat(res.getDayworks().size()).isLessThanOrEqualTo(3);
            assertThat(res.getDayworks()).isSortedAccordingTo(Comparator.comparingInt(o -> o.getPriority().getValue()));
        });

    }

    @Test
    @DisplayName("최근 내역 조회 시 조회일을 기준으로 최근 5일의 Account와 Daywork를 조회한다." +
            "GET /v1/schedule/{YYYYMM}/recent/0")
    void retrieve_recent_accounts_and_dayworks() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/schedule/{YYYYMM}/recent",
                                String.format("%d%02d", YEAR, MONTH))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        String content = mapper.readTree(json).get("content").toString();
        List<RecentScheduleResponseDto> response = mapper.readValue(content, new TypeReference<>(){});

        assertThat(response.size()).isLessThanOrEqualTo(5);
        assertThat(response).isSortedAccordingTo((o1, o2) -> {
            if (o1.getDate().isBefore(o2.getDate())) return 1;
            if (o1.getDate().isAfter(o2.getDate())) return -1;
            return 0;
        });
        response.forEach(res -> {
            assertThat(res.getAccounts()).isSortedAccordingTo(Comparator.comparingInt(o -> o.getType().getValue()));
            assertThat(res.getDayworks()).isSortedAccordingTo(Comparator.comparingInt(o -> o.getPriority().getValue()));
        });
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
