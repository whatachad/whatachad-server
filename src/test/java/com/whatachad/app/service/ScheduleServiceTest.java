package com.whatachad.app.service;

import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.repository.DayworkRepository;
import com.whatachad.app.type.DayworkPriority;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class ScheduleServiceTest {
    @Autowired private ScheduleService scheduleService;
    @Autowired private DayworkService dayworkService;
    @Autowired private TokenService tokenService;

    @Autowired private DayworkRepository dayworkRepository;

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
    public void Schedule와Daywork리스트가져오기() throws Exception{
        ScheduleDto scheduleDto = ScheduleDto.builder().year(2023).month(4).build();
        List<Daywork> dayworks = scheduleService.getDayworksBySchedule(scheduleDto);

//        dayworks.stream().forEach((i) -> System.out.println(i.getTitle() + " " + i.getDateTime().getDate()));
        Assertions.assertEquals(dayworks.size(), 8);
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
        DateTime dateTime = DateTime.builder()
                .date(date)
                .hour(11)
                .minute(50)
                .build();

        String title = "스쿼트";
        DayworkDto dayworkDto = DayworkDto.builder()
                .title(title)
                .priority(DayworkPriority.FIRST)
                .dateTime(dateTime)
                .build();

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(year)
                .month(month)
                .build();
        scheduleService.createDayworkOnSchedule(dayworkDto, scheduleDto);
    }
}
