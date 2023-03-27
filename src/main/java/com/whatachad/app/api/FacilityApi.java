package com.whatachad.app.api;

import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.CreateFacilityResponseDto;
import com.whatachad.app.model.response.FacilityResponseDto;
import com.whatachad.app.model.response.UpdateFacilityResponseDto;
import com.whatachad.app.type.FacilityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Facility API", description = "스포츠 시설 관련 기능")
@RequestMapping("/v1/facilities")
public interface FacilityApi {

    @Operation(summary = "스포츠 시설 등록",
            description = "유저가 직접 주소 정보나 지도 api 마커를 이용해 스포츠 시설을 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CreateFacilityResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping
    ResponseEntity<CreateFacilityResponseDto> registerFacility(@RequestBody @Valid CreateFacilityRequestDto requestDto);


    // TODO : 위치 기반 조회 기능 추가
    @Operation(summary = "주변 스포츠 시설 조회",
            description = "유저의 위치를 기반으로 주변 스포츠 시설을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FacilityResponseSlice.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping
    ResponseEntity<Slice<FacilityResponseDto>> getFacilities(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                   @RequestParam @Valid FacilityType category);


    // TODO : 검색 조건 추가
    @Operation(summary = "선택 지역 내의 스포츠 시설 조회",
            description = "유저가 지정한 지역 내의 주변 스포츠 시설을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FacilityResponseSlice.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping("/search")
    ResponseEntity<Slice<FacilityResponseDto>> getFacilitiesBySearchCond(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                                   @RequestParam(required = true) @Valid String l1,
                                                                   @RequestParam(required = false) @Valid String l2,
                                                                   @RequestParam(required = false) @Valid String l3);



    @Operation(summary = "스포츠 시설 상세 조회",
            description = "유저가 선택한 스포츠 시설에 대한 상세 정보를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = FacilityResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping("/{facilityId}")
    ResponseEntity<FacilityResponseDto> getFacility(@PathVariable Long facilityId);



    @Operation(summary = "스포츠 시설 정보 수정",
            description = "해당 시설을 등록한 유저가 시설 정보를 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = UpdateFacilityResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping
    ResponseEntity<UpdateFacilityResponseDto> editFacility(@RequestBody @Valid UpdateFacilityRequestDto requestDto);



    @Operation(summary = "등록된 스포츠 시설 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/{facilityId}")
    void deleteFacility(@PathVariable Long facilityId);

}

class FacilityResponseSlice extends SliceImpl<FacilityResponseDto> {
    public FacilityResponseSlice(List<FacilityResponseDto> content, Pageable pageable, boolean hasNext) {
        super(content, pageable, hasNext);
    }
}
