package com.whatachad.app.init;

import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.AccountDto;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.model.vo.Address;
import com.whatachad.app.repository.FacilityRepository;
import com.whatachad.app.security.AuthConstant;
import com.whatachad.app.service.ScheduleService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.service.UserService;
import com.whatachad.app.type.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.whatachad.app.type.AccountCategory.*;
import static com.whatachad.app.type.DayworkPriority.*;

@Component
@RequiredArgsConstructor
public class TestInit {

    public static final String[] FACILITY_TITLE = new String[]
            {"영휘트니스", "짐박스피트니스 신림역점",
                    "자마이카 피트니스 신림역점", "스포애니 보라매점",
                    "파운드짐 신림역점", "익스트림에스 신림점", "짐인더하우스 2호점"};
    public static final double[][] LAT_LNG = new double[][]{
            {37.4846553, 126.9272265},
            {37.4832519, 126.9287583},
            {37.484787, 126.9300366},
            {37.489879, 126.9271118},
            {37.4854721, 126.9299512},
            {37.484847, 126.9328386},
            {37.4905415, 126.9270999}
    };
    public static final String[][] ADDRESS = new String[][]{
            {"서울특별시 관악구 신림로 206", "서울특별시 관악구 신림동 96-3", "1162010200"},
            {"서울특별시 관악구 신림로59길 14", "서울특별시 관악구 신림동 1640-31", "1162010200"},
            {"서울특별시 관악구 신림로 340", "서울특별시 관악구 신림동 1422-5", "1162010200"},
            {"서울특별시 관악구 봉천로 227", "서울특별시 관악구 봉천동 972-5", "1162010100"},
            {"서울특별시 관악구 신림로 350", "서울특별시 관악구 신림동 1424-28", "1162010200"},
            {"서울특별시 관악구 남부순환로 1641", "서울특별시 관악구 신림동 1412-3", "1162010200"},
            {"서울특별시 관악구 보라매로 13", "서울특별시 관악구 봉천동 702-49", "1162010100"}
    };

    private final ObjectProvider<OrderedPostConstruct> initializers;

    @EventListener(ApplicationReadyEvent.class)
    public void initEntryPoint() {
        initializers.orderedStream().forEach(OrderedPostConstruct::init);
    }

    @Component
    @Order(1)
    @RequiredArgsConstructor
    static class UserInit implements OrderedPostConstruct {

        private final UserService userService;
        private final TokenService tokenService;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void init() {
            User user = User.builder()
                    .id(AuthConstant.ADMIN_USER)
                    .password(passwordEncoder.encode(AuthConstant.ADMIN_PWD))
                    .email("master.whatachad@gmail.com")
                    .name("master")
                    .phone("01012345678")
                    .meta(new HashMap<>())
                    .build();
            user.getMeta().put(UserMetaType.ROLE, UserRoleType.ROLE_ADMIN.name());
            userService.createUser(user);
            authorize();
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

    @Component
    @Order(2)
    @RequiredArgsConstructor
    static class FacilityInit implements OrderedPostConstruct {

        private final UserService userService;
        private final FacilityRepository facilityRepository;

        @Override
        public void init() {
            User adminUser = userService.getUser("admin");
            IntStream.range(0, FACILITY_TITLE.length)
                    .forEach(i -> {
                        FacilityDto facilityDto = FacilityDto.builder()
                                .address(Address.builder()
                                        .jibunAddress(ADDRESS[i][0])
                                        .roadAddress(ADDRESS[i][1])
                                        .regionCode(ADDRESS[i][2])
                                        .latitude(LAT_LNG[i][0])
                                        .longitude(LAT_LNG[i][1])
                                        .build())
                                .category(FacilityType.HEALTH)
                                .title(FACILITY_TITLE[i])
                                .build();
                        facilityRepository.save(Facility.create(adminUser, facilityDto));
                    });
        }
    }

    @Component
    @Order(3)
    @RequiredArgsConstructor
    static class ScheduleInit implements OrderedPostConstruct {

        private static final int YEAR = LocalDate.now().getYear();
        private static final int MONTH = LocalDate.now().getMonthValue();
        private static final AccountCategory[] CATEGORIES = {SALARY, INTEREST, ALLOWANCE, FOOD, RESIDENCE, INSURANCE};
        private static final DayworkPriority[] PRIORITIES = {FIRST, SECOND, THIRD};
        private static final int BUDGET = 500000;

        private final ScheduleService scheduleService;

        @Override
        public void init() {
            Random rand = new Random();
            // account 테스트 데이터 50개
            IntStream.rangeClosed(1, 50)
                    .forEach(tryCount -> {
                        int day = 1 + rand.nextInt(30); // 1 ~ 30일 중 하나
                        AccountCategory accountCategory = CATEGORIES[rand.nextInt(CATEGORIES.length)];
                        AccountDto accountDto = AccountDto.builder()
                                .title("ACCOUNT-" + tryCount)
                                .category(accountCategory)
                                .type(accountCategory.getType())
                                .cost(10000 + rand.nextInt(901) * 100) // 10,000 ~ 100,000 원
                                .build();
                        ScheduleDto scheduleDto = ScheduleDto.builder()
                                .year(YEAR)
                                .month(MONTH)
                                .budget(BUDGET)
                                .build();
                        scheduleService.createAccountOnSchedule(day, accountDto, scheduleDto);
                    });

            // daywork 테스트 데이터 50개
            IntStream.rangeClosed(1, 50)
                    .forEach(tryCount -> {
                        int day = 1 + rand.nextInt(30); // 1 ~ 30일 중 하나
                        DayworkDto dayworkDto = DayworkDto.builder()
                                .title("DAYWORK-" + tryCount)
                                .priority(PRIORITIES[rand.nextInt(PRIORITIES.length)])
                                .build();
                        ScheduleDto scheduleDto = ScheduleDto.builder()
                                .year(YEAR)
                                .month(MONTH)
                                .build();
                        scheduleService.createDayworkOnSchedule(day, dayworkDto, scheduleDto);
                    });
        }
    }

}
