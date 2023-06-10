package com.whatachad.app.model.mapper;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.vo.Address;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.request.FindFacilityDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.FacilityResponseDto;
import com.whatachad.app.type.FacilityType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FacilityConverter {

    public static final Map<String, String> REGION_CODE = new HashMap<>(); // <ADDRESS, REGION_CODE>
    public static final Map<String, String> SIDO_FULL_NAME = new HashMap<>(); // <ABBREVIATION_NAME, FULL_NAME>

    private final ResourceLoader resourceLoader;
    private final FacilityMapper facilityMapper;

    //== initialization methods ==//
    @PostConstruct
    void init() throws IOException {
        initRegionCode();
        initRegionAbbreviation();
    }

    // dto -> adaptee 변환
    public FacilityDto toFacilityDto(CreateFacilityRequestDto dto) {
        FacilityDto adaptee = facilityMapper.preprocess(dto);
        adaptee.setAddress(createAddress(dto));
        return adaptee;
    }

    public FacilityDto toFacilityDto(UpdateFacilityRequestDto dto) {
        FacilityDto adaptee = facilityMapper.preprocess(dto);
        adaptee.setAddress(createAddress(dto));
        return adaptee;
    }

    // 조회용 dto 변환
    public FindFacilityDto toFindFacilityDto(Map<String, String> findFacilityParam) {
        Map<String, String> validParam = getValidatedParam(findFacilityParam);
        return FindFacilityDto.builder()
                .category(FacilityType.valueOf(validParam.get("category")))
                .latitude(Double.valueOf(validParam.get("latitude")))
                .longitude(Double.valueOf(validParam.get("longitude")))
                .distance(Integer.valueOf(validParam.get("distance")))
                .build();
    }

    // 클라이언트 응답 dto 변환
    public FacilityResponseDto toResponseDto(Facility entity) {
        return facilityMapper.toResponseDto(entity);
    }

    public String getRegionCode(String jibunAddress) {
        String[] splitAddress = jibunAddress.split(" ");
        splitAddress[0] = SIDO_FULL_NAME.getOrDefault(splitAddress[0], splitAddress[0]); // 시도 약칭을 정식명칭으로 변환
        String validAddress = Arrays.stream(splitAddress)
                .limit(splitAddress.length - 1)
                .filter(s -> !s.equals("산")) // 지번 앞에 산이 있는 경우 산을 제외한다.
                .collect(Collectors.joining(" "));
        String regionCode = REGION_CODE.get(validAddress);
        if (Objects.nonNull(regionCode)) {
            return regionCode;
        }
        throw new CommonException(BError.NOT_VALID, "address");
    }

    //== private methods ==//
    private void initRegionCode() throws IOException {
        BufferedReader reader = getResourcesReader("static/region-code.csv");
        String currentLine = reader.readLine(); // 첫 줄은 column명이므로 skip
        while ((currentLine = reader.readLine()) != null) {
            String[] splitLine = currentLine.split(",");
            String regionCode = splitLine[0];
            String area = String.join(" ",
                    splitLine[1], splitLine[2], splitLine[3], splitLine[4]).trim();
            REGION_CODE.put(area, regionCode);
        }
        reader.close();
    }

    private void initRegionAbbreviation() throws IOException {
        BufferedReader reader = getResourcesReader("static/region-abbreviation.csv");
        String currentLine = reader.readLine();
        while ((currentLine = reader.readLine()) != null) {
            String[] splitLine = currentLine.split(",");
            SIDO_FULL_NAME.put(splitLine[0], splitLine[1]);
        }
        reader.close();
    }

    private BufferedReader getResourcesReader(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        InputStream inputStream = resource.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader;
    }



    private Address createAddress(CreateFacilityRequestDto dto) {
        return Address.builder()
                .regionCode(getRegionCode(dto.getJibunAddress()))
                .roadAddress(dto.getRoadAddress())
                .jibunAddress(dto.getJibunAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    private Address createAddress(UpdateFacilityRequestDto dto) {
        return Address.builder()
                .regionCode(getRegionCode(dto.getJibunAddress()))
                .roadAddress(dto.getRoadAddress())
                .jibunAddress(dto.getJibunAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    private Map<String, String> getValidatedParam(Map<String, String> findFacilityParam) {
        Map<String, String> validParam = new HashMap<>();
        String[] keys = new String[]{"category", "latitude", "longitude", "distance"};
        Arrays.stream(keys)
                .forEach(k -> {
                    validParam.put(k, null);
                });
        validParam.putAll(findFacilityParam);
        validParam.entrySet()
                .forEach(e -> validate(e));
        return validParam;
    }

    private void validate(Map.Entry entry) {
        // TODO : 존재하지 않는 카테고리, 잘못된 형식의 위도/경도, 거리 타입 검증 등
        // 카테고리 값이 존재하지 않는다면 "HEALTH"를 default로서 지정한다.
        Object value = entry.getValue();
        if (entry.getKey().equals("category")) {
            if (value == null) {
                entry.setValue("HEALTH");
            } else if (!FacilityType.contains((String) value)) {
                throw new CommonException(BError.NOT_VALID, (String) entry.getKey());
            }
        } else {
            if (value == null) {
                // 다른 파라미터의 값이 존재하지 않는다면 예외를 던진다.
                throw new CommonException(BError.NOT_EXIST, (String) entry.getKey());
            }
        }
    }


}
