package com.whatachad.app.api;

import com.whatachad.app.model.dto.ScheduleDto;
import com.whatachad.app.model.response.CreateFacilityResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Facility API", description = "스포츠 시설 관련 기능")
@RequestMapping("/v1/facilities")
public interface FacilityApi {

    @Operation(summary = "스포츠 시설 등록",
            description = "유저가 직접 주소 정보나 지도 api 마커를 이용해 스포츠 시설을 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CreateFacilityResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Invalid input")
    })
    @PostMapping
    ResponseEntity<CreateFacilityResponseDto> registerFacility();

}
