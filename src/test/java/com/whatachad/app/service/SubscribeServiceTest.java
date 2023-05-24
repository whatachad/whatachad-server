package com.whatachad.app.service;

import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.UserMetaType;
import com.whatachad.app.type.UserRoleType;
import com.whatachad.app.util.TestDataProcessor;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class SubscribeServiceTest {
    private static final int YEAR = LocalDateTime.now().getYear();
    private static final int MONTH = LocalDateTime.now().getMonthValue();
    private static final int DAY = LocalDate.now().getDayOfMonth();

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    private TestDataProcessor processor;

    @BeforeAll
    void initSchedule() {
        // 유저 생성
        User userA = createUserA();
        User userB = createUserB();

        // userA의 Daywork 생성
        loginUserA();
        DayworkDto dayworkDto = DayworkDto.builder().title("밥먹기").priority(DayworkPriority.FIRST).build();
        ScheduleDto scheduleDto = ScheduleDto.builder().year(YEAR).month(MONTH).build();
        scheduleService.createDayworkOnSchedule(DAY, dayworkDto, scheduleDto);
        scheduleService.createDayworkOnSchedule(DAY + 1, dayworkDto, scheduleDto);
        scheduleService.createDayworkOnSchedule(DAY + 2, dayworkDto, scheduleDto);


        // 팔로우 관계 만들기
        loginAdmin();
        subscribeService.createFollow("userA");
        subscribeService.createFollow("userB");
    }

    @AfterAll
    void rollback() {
        processor.rollback();
    }

    @BeforeEach
    void init() {
        loginAdmin();
    }
    
    @Test
    @DisplayName("로그인된 유저가 following한 유저 목록을 가져온다.")
    public void getFollowingUsers() {
        List<User> followingUser = userService.getFollowingUser();
        assertThat(followingUser)
                .hasSize(2)
                .extracting(User::getId)
                .containsExactlyInAnyOrder("userA", "userB");
    }

    @Test
    @DisplayName("로그인된 유저가 following한 유저가 오늘 날짜의 DaySchedule을 생성했다면 조회한다.")
    public void getFollowingUsersTodayDaySchedule() {
        List<User> followingUser = userService.getFollowingUser();
        List<String> followingsId = followingUser.stream().map(User::getId).toList();
        Map<String, DaySchedule> followingsAndTodayStatus = subscribeService.getFollowingsAndTodayStatus(followingsId);
        assertThat(followingsAndTodayStatus).hasSize(1);
        assertThat(followingsAndTodayStatus.keySet()).contains("userA");
        assertThat(followingsAndTodayStatus.get("userA").getDay()).isEqualTo(DAY);
    }

    /**
     *  private Method
     * */
    private void loginUserA() {
        UserLoginRequestDto loginDto = UserLoginRequestDto.builder()
                .id("userA")
                .password("passwordA")
                .build();
        login(loginDto);
    }

    private void loginUserB() {
        UserLoginRequestDto loginDto = UserLoginRequestDto.builder()
                .id("userB")
                .password("passwordB")
                .build();
        login(loginDto);
    }

    private void loginAdmin() {
        UserLoginRequestDto loginDto = UserLoginRequestDto.builder()
                .id("admin")
                .password("admin")
                .build();
        login(loginDto);
    }
    private User createUserA() {
        User user = User.builder()
                .id("userA")
                .password(passwordEncoder.encode("passwordA"))
                .email("userA@gmail.com")
                .name("nameA")
                .phone("01011111111")
                .meta(new HashMap<>())
                .build();
        user.getMeta().put(UserMetaType.ROLE, UserRoleType.ROLE_CUSTOMER.name());
        return userService.createUser(user);
    }

    private User createUserB() {
        User user = User.builder()
                .id("userB")
                .password(passwordEncoder.encode("passwordB"))
                .email("userB@gmail.com")
                .name("nameB")
                .phone("01022222222")
                .valid(true)
                .meta(new HashMap<>())
                .build();
        user.getMeta().put(UserMetaType.ROLE, UserRoleType.ROLE_CUSTOMER.name());
        return userService.createUser(user);
    }

    private void login(UserLoginRequestDto loginDto) {
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


