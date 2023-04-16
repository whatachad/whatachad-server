package com.whatachad.app.service;

import com.whatachad.app.model.domain.Re_Schedule;
import com.whatachad.app.model.dto.Re_ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class Re_ScheduleServiceTest {
    @Autowired private Re_ScheduleService scheduleService;
    @Autowired private TokenService tokenService;

    Re_Schedule schedule;

    @BeforeEach
    void init() throws Exception{
        authorize();
        Integer year = 2023;
        Integer month = 4;
        Re_ScheduleDto dto = Re_ScheduleDto.builder()
                .year(year)
                .month(month)
                .build();
        schedule = scheduleService.getOrCreateSchedule(dto);
    }

    @Test
    public void getOrCreateSchedule() {
        Integer year = 2023;
        Integer month = 4;
        Re_ScheduleDto dto = Re_ScheduleDto.builder()
                .year(year)
                .month(month)
                .build();
        Re_Schedule schedule = scheduleService.getOrCreateSchedule(dto);
        assertEquals(schedule.getYear(), year);
        assertEquals(schedule.getMonth(), month);

        Re_Schedule schedule2 = scheduleService.getOrCreateSchedule(dto);
        assertEquals(schedule.getId(), schedule2.getId());
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
