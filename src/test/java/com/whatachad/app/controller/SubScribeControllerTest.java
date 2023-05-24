package com.whatachad.app.controller;

import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.service.ScheduleService;
import com.whatachad.app.service.SubscribeService;
import com.whatachad.app.service.TokenService;
import com.whatachad.app.service.UserService;
import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.UserMetaType;
import com.whatachad.app.type.UserRoleType;
import com.whatachad.app.util.TestDataProcessor;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubScribeControllerTest {
    private static final int YEAR = LocalDateTime.now().getYear();
    private static final int MONTH = LocalDateTime.now().getMonthValue();
    private static final int DAY = LocalDate.now().getDayOfMonth();
    private String accessToken;

    @Autowired
    private MockMvc mockMvc;
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
    TestDataProcessor processor;

    @BeforeAll
    void initSchedule() {
        // 유저 생성
        User userA = createUserA();

        // UserA의 Daywork 생성
        loginUserA();
        DayworkDto dayworkDto = DayworkDto.builder().title("밥먹기").priority(DayworkPriority.FIRST).build();
        ScheduleDto scheduleDto = ScheduleDto.builder().year(YEAR).month(MONTH).build();
        scheduleService.createDayworkOnSchedule(DAY, dayworkDto, scheduleDto);
        scheduleService.createDayworkOnSchedule(DAY + 1, dayworkDto, scheduleDto);
        scheduleService.createDayworkOnSchedule(DAY + 2, dayworkDto, scheduleDto);

        // 팔로우 관계 만들기
        loginAdmin();
        subscribeService.createFollow("userA");
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
    @Order(1)
    @DisplayName("로그인 유저가 다른 유저를 follow 한다." +
            "POST /v1/subscribe/follow/{followingId}")
    void follow() throws Exception {
        User userB = createUserB();
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/subscribe/follow/" + userB.getId())
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    @Order(2)
    @DisplayName("기존에 follow한 유저를 unfollow 한다." +
            "DELETE /v1/subscribe/unfollow/{followingId}")
    void unfollow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/subscribe/unfollow/" + "userB")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));
    }

    @Test
    @Order(3)
    @DisplayName("로그인 유저가 팔로우한 유저 목록과 함께 각 유저가 오늘 할 일을 완료했는지의 여부를 확인한다. " +
            "GET /v1/subscribe/followings")
    void getFollowingUsersAndTodayDayScheduleComplete() throws Exception {
        String expectByUserId = "$.[?(@.id == '%s')]";
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/subscribe/followings")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath(expectByUserId, "userA").exists())
                .andExpect(jsonPath("$.[?(@.id == 'userA')].todayDayworkStatus").value("NOT_COMPLETE"));
    }

    /**
     * private Method
     */
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
        accessToken = token.getAccessToken();
        Claims claims = tokenService.validateToken(token.getAccessToken());
        List<String> authorities = (List) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
