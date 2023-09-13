package com.whatachad.app.service;

import com.whatachad.app.model.response.ReverseGeocodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class LocalMapService {

    private static final double EARTH_RADIUS = 6371;
    private static final String REVERSE_GEOCODE_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json";
    @Qualifier("localMapRestTemplate")
    private final RestTemplate restTemplate;


    public String reverseGeocode(double lat, double lng) {
        String requestUrl = UriComponentsBuilder.fromHttpUrl(REVERSE_GEOCODE_API_URL)
                .queryParam("x", lng)
                .queryParam("y", lat)
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        ReverseGeocodeDto response = restTemplate.getForObject(requestUrl, ReverseGeocodeDto.class);
        assert response != null;
        if (response.getDocuments().size() == 0) return null;
        return response
                .getDocuments()
                .get(0)
                .getAddress()
                .getAddress_name();
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
                .filter(Objects::nonNull)
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
