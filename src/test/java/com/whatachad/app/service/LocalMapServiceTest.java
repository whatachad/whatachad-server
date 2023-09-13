package com.whatachad.app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LocalMapServiceTest {

    @Autowired
    private LocalMapService localMapService;

    @Test
    @DisplayName("좌표를 주소로 변환하는 REST API 테스트")
    void get_jibun_address_from_coordinates() throws Exception {
        String jibunAddress = localMapService.reverseGeocode(37.4846553, 126.9272265);
        assertThat(jibunAddress).isEqualTo("서울 관악구 신림동 1433-95");
    }
}
