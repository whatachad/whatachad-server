package com.whatachad.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.response.ReverseGeocodeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocalMapService {

    private static final double EARTH_RADIUS = 6371;
    private static final String REST_API_KEY = "58d92c624b9a0af0d99c01bece4f2b3b";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public String reverseGeocode(double lat, double lng) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
            requestUrl += "?y=" + lat;
            requestUrl += "&x=" + lng;

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "KakaoAK " + REST_API_KEY);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> response = restTemplate
                    .exchange(requestUrl, HttpMethod.GET, httpEntity, String.class);
            ReverseGeocodeDto reverseGeocodeDto = objectMapper.readValue(response.getBody(), ReverseGeocodeDto.class);
            return reverseGeocodeDto
                    .getDocuments()
                    .get(0)
                    .getAddress()
                    .getAddress_name();
        } catch (JsonProcessingException e) {
            throw new CommonException(BError.FAIL_REASON, "reverse-geocode",
                    "cannot parse reverse-geocode response");
        }
    }

    /**
     * 주어진 좌표의 주소와 그로부터 동서남북 방향으로 {distance}만큼 떨어진 곳의 주소를 구하는 메서드
     * @param lat
     * @param lng
     * @param distance
     * @return
     */
    public String[] findAddressesWith4Directions(double lat, double lng, double distance) {
        List<double[]> coordinates = IntStream.iterate(0, d -> d < 360, d -> d + 90)
                .mapToObj(d -> calculateCoordinate(lat, lng, distance, d))
                .collect(Collectors.toList());
        coordinates.add(new double[]{lat, lng});
        return coordinates.stream()
                .map(c -> reverseGeocode(c[0], c[1]))
                .toArray(String[]::new);
    }

    /**
     * 주어진 좌표로부터 특정 방향으로 떨어진 지점의 좌표를 구하는 메서드
     * @param lat (위도, y)
     * @param lng (경도, x)
     * @param distance (km)
     * @param bearing (0: 북쪽, 90: 동쪽, 180: 남쪽, 270: 서쪽)
     */
    public double[] calculateCoordinate(double lat, double lng, double distance, double bearing) {
        double[] newCoordinate = new double[2];

        double angularDistance = distance / EARTH_RADIUS;
        double bearingRad = Math.toRadians(bearing);
        double latRad = Math.toRadians(lat);
        double lngRad = Math.toRadians(lng);

        double newLatRad = Math.asin(Math.sin(latRad) * Math.cos(angularDistance) +
                Math.cos(latRad) * Math.sin(angularDistance) * Math.cos(bearingRad));
        double newLngRad = lngRad + Math.atan2(Math.sin(bearingRad) * Math.sin(angularDistance) *
                Math.cos(latRad), Math.cos(angularDistance) - Math.sin(latRad) *
                Math.sin(newLatRad));

        newCoordinate[0] = Math.toDegrees(newLatRad);
        newCoordinate[1] = Math.toDegrees(newLngRad);

        return newCoordinate;
    }
}
