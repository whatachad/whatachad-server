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

@Slf4j
@RequiredArgsConstructor
@Service
public class LocalMapService {

    private static final String REST_API_KEY = "58d92c624b9a0af0d99c01bece4f2b3b";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public String reverseGeocode(Double lat, Double lng) {
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
}
