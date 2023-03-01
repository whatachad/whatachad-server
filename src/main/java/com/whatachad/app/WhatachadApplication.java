package com.whatachad.app;

import com.whatachad.app.model.domain.User;
import com.whatachad.app.security.AuthConstant;
import com.whatachad.app.service.UserService;
import com.whatachad.app.type.UserMetaType;
import com.whatachad.app.type.UserRoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;

@EnableJpaAuditing
@SpringBootApplication
public class WhatachadApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatachadApplication.class, args);

    }

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

}
