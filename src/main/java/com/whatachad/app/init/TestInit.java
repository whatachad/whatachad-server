package com.whatachad.app.init;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.repository.FacilityRepository;
import com.whatachad.app.security.AuthConstant;
import com.whatachad.app.service.UserService;
import com.whatachad.app.type.FacilityType;
import com.whatachad.app.type.UserMetaType;
import com.whatachad.app.type.UserRoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.stream.IntStream;

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
        }
    }
    @Component
    @Order(3)
    @RequiredArgsConstructor
    static class UserInit2 implements OrderedPostConstruct {

        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void init() {
            User user2 = User.builder()
                    .id("user")
                    .password(passwordEncoder.encode("user"))
                    .email("khjung1654@gmail.com")
                    .name("user")
                    .phone("01012345679")
                    .valid(true)
                    .meta(new HashMap<>())
                    .build();
            user2.getMeta().put(UserMetaType.ROLE, UserRoleType.ROLE_CUSTOMER.name());
            userService.createUser(user2);
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
}
