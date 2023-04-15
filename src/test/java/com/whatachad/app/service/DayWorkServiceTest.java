package com.whatachad.app.service;

import com.whatachad.app.model.domain.DateTime;
import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
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
public class DayWorkServiceTest {

    @Autowired DayworkService dayworkService;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    void initFacility() throws Exception {
        authorize();
    }

    @Test
    public void 기존에Schedule없을때Daywork생성() throws Exception{
        //given
        DateTime dateTime = DateTime.builder()
                .date(1)
                .hour(11)
                .minute(50)
                .build();

        String title = "스쿼트";
        DayworkDto dayworkDto = DayworkDto.builder()
                .title(title)
                .priority(DayworkPriority.FIRST)
                .dateTime(dateTime)
                .build();

        Integer year = 2023;
        Integer month = 3;

        ScheduleDto scheduleDto = ScheduleDto.builder()
                .year(year)
                .month(month)
                .build();

        //when
        //Daywork daywork = dayworkService.createDaywork(dayworkDto, scheduleDto);
        //Schedule dayworkSchedule = daywork.getSchedule();

        //then
        //Assertions.assertEquals(daywork.getTitle(), title);
        //Assertions.assertEquals(dayworkSchedule.getYear(), year);
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
