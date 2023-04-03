package com.whatachad.app;

import com.whatachad.app.model.domain.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.repository.FacilityRepository;
import com.whatachad.app.security.AuthConstant;
import com.whatachad.app.service.UserService;
import com.whatachad.app.type.FacilityType;
import com.whatachad.app.type.UserMetaType;
import com.whatachad.app.type.UserRoleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class TestInit {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final FacilityRepository facilityRepository;

    @Order(value = 1)
    @PostConstruct
    void initAdminUser() {
        User user = User.builder()
                .id(AuthConstant.ADMIN_USER)
                .password(passwordEncoder.encode(AuthConstant.ADMIN_PWD))
                .email("whatachad123@gmail.com")
                .name("master")
                .phone("01012345678")
                .meta(new HashMap<>())
                .build();
        user.getMeta().put(UserMetaType.ROLE, UserRoleType.ROLE_ADMIN.name());
        userService.createUser(user);
    }

    @Order(value = 2)
    @PostConstruct
    void initTestFacilities() {
        String[] title = new String[]{"영휘트니스", "짐박스피트니스 신림역점", "자마이카 피트니스 신림역점", "스포애니 보라매점", "파운드짐 신림역점", "익스트림에스 신림점", "짐인더하우 2호점"};
        double[][] latLng = new double[][]{
                {37.4846553, 126.9272265},
                {37.4832519, 126.9287583},
                {37.484787, 126.9300366},
                {37.489879, 126.9271118},
                {37.4854721, 126.9299512},
                {37.484847, 126.9328386},
                {37.4905415, 126.9270999}
        };
        String[][] address = new String[][]{
                {"서울특별시 관악구 신림로 206", "서울특별시 관악구 신림동 96-3", "1162010200"},
                {"서울특별시 관악구 신림로59길 14", "서울특별시 관악구 신림동 1640-31", "1162010200"},
                {"서울특별시 관악구 신림로 340", "서울특별시 관악구 신림동 1422-5", "1162010200"},
                {"서울특별시 관악구 봉천로 227", "서울특별시 관악구 봉천동 972-5", "1162010100"},
                {"서울특별시 관악구 신림로 350", "서울특별시 관악구 신림동 1424-28", "1162010200"},
                {"서울특별시 관악구 남부순환로 1641", "서울특별시 관악구 신림동 1412-3", "1162010200"},
                {"서울특별시 관악구 보라매로 13", "서울특별시 관악구 봉천동 702-49", "1162010100"}
        };

        User adminUser = userService.getUser("admin");
        IntStream.range(0, title.length)
                .forEach(i -> {
                    FacilityDto facilityDto = FacilityDto.builder()
                            .address(Address.builder()
                                    .jibunAddress(address[i][0])
                                    .roadAddress(address[i][1])
                                    .regionCode(address[i][2])
                                    .latitude(latLng[i][0])
                                    .longitude(latLng[i][1])
                                    .build())
                            .category(FacilityType.HEALTH)
                            .title(title[i])
                            .build();
                    facilityRepository.save(Facility.create(adminUser, facilityDto));
                });
    }
}
