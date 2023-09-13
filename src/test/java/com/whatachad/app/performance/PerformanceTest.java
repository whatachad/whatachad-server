package com.whatachad.app.performance;

import com.whatachad.app.common.CommonException;
import com.whatachad.app.init.TestInit;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.mapper.FacilityConverter;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.repository.FacilityRepository;
import com.whatachad.app.service.LocalMapService;
import com.whatachad.app.service.UserService;
import com.whatachad.app.type.FacilityType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PerformanceTest {

    static int COUNT = 0;

    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private LocalMapService localMapService;
    @Autowired
    private UserService userService;
    @Autowired
    private FacilityConverter facilityConverter;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test01() {
        // 랜덤으로 추출한 거리만큼 떨어진 곳의 주소 찾기
        Random rand = new Random();
        double[] coordinate = {37.6472082, 126.8952575};
        // 50 m 간격으로 1,000 x 1,000 그리드를 만들고 random facility 생성 (invalid address는 통과, 100만개 채울 때까지 반복)
        while (COUNT < 1000000) {
            double[] start = new double[2];
            System.arraycopy(coordinate, 0, start, 0, 2);
            for (int i = 0; i < 1000; i++) {
                try {
                    createRandomFacility(coordinate);
                } catch (CommonException e) {
                    System.out.println(coordinate);
                }
                coordinate = getEasternCoordinate(coordinate, 0.05);
            }
            coordinate = getSouthernCoordinate(start, 0.05);
        }
    }

    @Test
    @DisplayName("사용자 주변 스포츠 시설을 조회한다. (DB에서 직접 거리 계산)"  +
            "GET /v1/facilities?page=0&size=5&category=HEALTH&latitude=37.484231&longitude=126.929699&distance=1000")
    void find_facilities_around_user_v1() throws Exception {
        long start = System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities")
                        .param("page", "0")
                        .param("size", "5")
                        .param("category", "HEALTH")
                        .param("latitude", String.valueOf(TestInit.LAT_LNG[0][0]))
                        .param("longitude", String.valueOf(TestInit.LAT_LNG[0][1]))
                        .param("distance", "100")
                        .header("Authorization", "debug"))
                .andExpect(status().isOk());
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    @DisplayName("사용자 주변 스포츠 시설을 조회한다. (지역코드 이용하는 버전) " +
            "GET /v1/facilities/v2?page=0&size=5&category=HEALTH&latitude=37.4846553&longitude=126.9272265&distance=1")
    void find_facilities_around_user_v2() throws Exception {
        long start = System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/facilities/v2")
                        .param("page", "0")
                        .param("size", "5")
                        .param("category", "HEALTH")
                        .param("latitude", String.valueOf(TestInit.LAT_LNG[0][0]))
                        .param("longitude", String.valueOf(TestInit.LAT_LNG[0][1]))
                        .param("distance", "1")
                        .header("Authorization", "debug"))
                .andExpect(status().isOk());
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private void createRandomFacility(double[] coordinate) {
        User adminUser = userService.getUser("admin");
        String address = localMapService.reverseGeocode(coordinate[0], coordinate[1]);
        if (address == null) return;
        CreateFacilityRequestDto createDto = CreateFacilityRequestDto.builder()
                .jibunAddress(address)
                .latitude(coordinate[0])
                .longitude(coordinate[1])
                .category(FacilityType.HEALTH)
                .title("FACILITY-" + COUNT++)
                .build();
        FacilityDto facilityDto = facilityConverter.toFacilityDto(createDto);
        facilityRepository.save(Facility.create(adminUser, facilityDto));
    }

    private double[] getEasternCoordinate(double[] coordinate, double distance) {
        return localMapService.calculateCoordinate(coordinate[0], coordinate[1], distance, 90);
    }

    private double[] getSouthernCoordinate(double[] coordinate, double distance) {
        return localMapService.calculateCoordinate(coordinate[0], coordinate[1], distance, 180);
    }
}
