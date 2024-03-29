package com.whatachad.app.service;

import com.whatachad.app.model.domain.Account;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.repository.ScheduleRepository;
import com.whatachad.app.type.AccountCategory;
import com.whatachad.app.type.AccountType;
import com.whatachad.app.util.TestDataProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DayScheduleServiceTest {

    private static final int YEAR = LocalDate.now().getYear();
    private static final int MONTH = LocalDate.now().getMonthValue();

    @Autowired
    private DayScheduleService dayScheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private TestDataProcessor processor;

    private Schedule schedule;

    @BeforeEach
    void initSchedule() {
        schedule = Schedule.builder()
                .year(YEAR)
                .month(MONTH)
                .budget(500000)
                .build();
        scheduleRepository.save(schedule);
    }

    @AfterEach
    void rollback() {
        processor.rollback();
    }

    @Test
    @DisplayName("주어진 날짜에 가계부를 생성한다.")
    void createAccountOnDay() {
        AccountDto accountDto = AccountDto.builder()
                .title("장보기")
                .cost(10000)
                .type(AccountType.SPEND)
                .category(AccountCategory.FOOD)
                .build();
        DaySchedule daySchedule = dayScheduleService.createAccountOnDay(1, accountDto, schedule.getId());

        Account account = daySchedule.getLastAccount();
        assertThat(account.getTitle()).isEqualTo("장보기");
        assertThat(account.getCost()).isEqualTo(10000);
        assertThat(account.getType()).isEqualTo(AccountType.SPEND);
        assertThat(account.getCategory()).isEqualTo(AccountCategory.FOOD);
    }

}
