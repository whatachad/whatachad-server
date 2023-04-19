package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.repository.DayworkRepository;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import com.whatachad.app.type.DayworkPriority;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenService tokenService;


    @BeforeEach
    void initSchedule() throws Exception {
        authorize();
        createDaywork(2023, 4, 2);
        createDaywork(2023, 4, 2);
        createDaywork(2023, 4, 2);
        createDaywork(2023, 4, 2);
        createDaywork(2023, 4, 2);
        createDaywork(2023, 4, 2);
        createDaywork(2023, 4, 2);
        createDaywork(2023, 4, 2);

        createDaywork(2023, 4, 1);
        createDaywork(2023, 4, 1);
        createDaywork(2023, 4, 1);
        createDaywork(2023, 4, 4);


        createDaywork(2023, 4, 1);
        createDaywork(2023, 4, 4);
    }


    @Test
    @DisplayName("Schedule을 통해 Account 생성시 accountDate가 올바르게 저장되어야 한다.")
    void exists_account_date_when_created_by_schedule() {
        AccountDto accountDto = AccountDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category(AccountCategory.FOOD)
                .build();
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(2023)
                .month(4)
                .budget(500000)
                .build();
        Account account = scheduleService.createAccountOnSchedule(1, accountDto, scheduleDto);

        String localDate = LocalDate.of(2023, 4, 1).toString();
        assertThat(account.getAccountDate()).isEqualTo(localDate);
    }

    @Test
    @DisplayName("Schedule을 통해 Account 생성 후 DB에서 Account 조회하면 accountDate가 저장되어 있어야 한다.")
    void must_have_account_date_when_retrieving_account_from_db() {
        AccountDto accountDto = AccountDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category(AccountCategory.FOOD)
                .build();
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(2023)
                .month(4)
                .budget(500000)
                .build();
        Account account = scheduleService.createAccountOnSchedule(1, accountDto, scheduleDto);
        Account findAccount = accountService.findAccountById(account.getId());

        String localDate = LocalDate.of(2023, 4, 1).toString();
        assertThat(findAccount.getAccountDate()).isEqualTo(localDate);

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
        Claims claims = tokenService.validateToken(token.getAccessToken());
        List<String> authorities = (List) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void createDaywork(int year, int month, int date) {

        String title = "스쿼트";
        DayworkDto dayworkDto = DayworkDto.builder()
                .title(title)
                .priority(DayworkPriority.FIRST)
                .build();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(year)
                .month(month)
                .build();
        scheduleService.createDayworkOnSchedule(dayworkDto, scheduleDto);
    }
}
