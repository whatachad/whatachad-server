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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.stream.IntStream;

@EnableJpaAuditing
@SpringBootApplication
public class WhatachadApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatachadApplication.class, args);

    }

    @Order(value = 1)
    @Bean
    public CommandLineRunner adminUser(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
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
        };
    }

    // TODO : 테스트 데이터 초기화를 위한 클래스 혹은 sql script 필요
    @Order(value = 2)
    @Bean
    public CommandLineRunner initTestData(FacilityRepository facilityRepository, UserService userService) {
        return args -> {

            String[] title = new String[]{"영휘트니스헬스클럽", "짐박스피트니스 신림역점", "자마이카짐", "에이오짐", "파운드짐 신림역점", "익스트림에스 신림"};
            double[][] latLng = new double[][]{
                    {37.4846553, 126.9272265},
                    {37.4832519, 126.9287583},
                    {37.484787, 126.9300366},
                    {37.4851178, 126.9302344},
                    {37.4854721, 126.9299512},
                    {37.484847, 126.9328386}
            };

            User adminUser = userService.getUser("admin");
            IntStream.range(0, 6)
                    .forEach(i -> {
                        FacilityDto facilityDto = FacilityDto.builder()
                                .address(Address.builder()
                                        .latitude(latLng[i][0])
                                        .longitude(latLng[i][1])
                                        .build())
                                .category(FacilityType.HEALTH)
                                .title(title[i])
                                .build();
                        facilityRepository.save(Facility.create(adminUser, facilityDto));
                    });
        };
    }
}
