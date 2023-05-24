package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.model.vo.AccountDayworkByDay;
import com.whatachad.app.model.vo.DayworkByDay;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class ScheduleServiceTest {

    private static final int YEAR = LocalDate.now().getYear();
    private static final int MONTH = LocalDate.now().getMonthValue();

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenService tokenService;

    private Schedule schedule;

    @BeforeEach
    void initSchedule() {
        authorize();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(YEAR)
                .month(MONTH)
                .build();
        schedule = scheduleService.findSchedule(scheduleDto);
    }

    @Test
    @DisplayName("Schedule을 통해 Account 생성시 accountDate가 올바르게 저장되어야 한다.")
    void exists_account_date_when_created_by_schedule() {
        AccountDto accountDto = AccountDto.builder()
                .accountDate(LocalDate.of(YEAR, MONTH, 1))
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
        Account account = scheduleService.createAccountOnSchedule(1, accountDto, scheduleDto);

        String localDate = LocalDate.of(YEAR, MONTH, 1).toString();
        assertThat(account.getAccountDate()).isEqualTo(localDate);
    }

    @Test
    @DisplayName("Schedule을 통해 특정 월의 Daywork를 날짜 순으로 가져온다.")
    void retrieve_dayworks_by_day_through_schedule() {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .id(schedule.getId())
                .year(YEAR)
                .month(MONTH)
                .build();

        List<DayworkByDay> dayworksOnSchedule = scheduleService.findDayworksOnSchedule(scheduleDto);
        assertThat(dayworksOnSchedule).isSortedAccordingTo((o1, o2) -> {
            if (o1.getDate().isBefore(o2.getDate())) return -1;
            if (o1.getDate().isAfter(o2.getDate())) return 1;
            return 0;
        });
    }

    @Test
    @DisplayName("Schedule을 통해 Account 생성 후 DB에서 Account 조회하면 accountDate가 저장되어 있어야 한다.")
    void must_have_account_date_when_retrieving_account_from_db() {
        AccountDto accountDto = AccountDto.builder()
                .accountDate(LocalDate.of(YEAR, MONTH, 1))
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
        Account account = scheduleService.createAccountOnSchedule(1, accountDto, scheduleDto);
        Account findAccount = accountService.findAccountById(account.getId());

        String localDate = LocalDate.of(YEAR, MONTH, 1).toString();
        assertThat(findAccount.getAccountDate()).isEqualTo(localDate);
    }

    @Test
    @DisplayName("최근 5일 내의 가계부와 할 일 목록을 최근 순서대로 조회한다.")
    void retrieve_recent_accounts_and_dayworks() {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .id(schedule.getId())
                .year(YEAR)
                .month(MONTH)
                .build();
        Slice<AccountDayworkByDay> allOnSchedule = scheduleService.findAllOnSchedule(scheduleDto, null);
        List<AccountDayworkByDay> content = allOnSchedule.getContent();

        assertThat(content.size()).isLessThanOrEqualTo(5);
        assertThat(content).isSortedAccordingTo((o1, o2) -> {
            if (o1.getDate().isBefore(o2.getDate())) return 1;
            if (o1.getDate().isAfter(o2.getDate())) return -1;
            return 0;
        });
    }

    @Test
    @DisplayName("가계부와 할 일을 생성하지 않았을 때 최근 내역을 조회하더라도 에러가 발생하지 않고 비어있는 값이 조회되어야 한다.")
    void retrieve_recent_not_existing_accounts_and_dayworks() {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .id(schedule.getId())
                .year(YEAR)
                .month((MONTH + 1) % 12)
                .build();

        Slice<AccountDayworkByDay> allOnSchedule = scheduleService.findAllOnSchedule(scheduleDto, null);
        List<AccountDayworkByDay> content = allOnSchedule.getContent();
        assertThat(content).isEmpty();
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

}
