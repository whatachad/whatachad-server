package com.whatachad.app.api;

import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.CreateFacilityResponseDto;
import com.whatachad.app.model.response.UpdateFacilityResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<CreateFacilityResponseDto> registerFacility(@RequestBody CreateFacilityRequestDto requestDto);

    @Operation(summary = "스포츠 시설 정보 수정",
            description = "해당 시설을 등록한 유저가 시설 정보를 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = UpdateFacilityResponseDto.class))),
            @ApiResponse(responseCode = "405", description = "Invalid input")
    })
    @PutMapping
    ResponseEntity<UpdateFacilityResponseDto> editFacility(@RequestBody UpdateFacilityRequestDto requestDto);

    @Operation(summary = "등록된 스포츠 시설 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "405", description = "Invalid input")
    })
    @DeleteMapping("/{facilityId}")
    void deleteFacility(@PathVariable Long facilityId);


}
