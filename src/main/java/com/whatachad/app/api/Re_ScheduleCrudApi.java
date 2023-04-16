package com.whatachad.app.api;

import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.Re_CreateDayworkRequestDto;
import com.whatachad.app.model.response.CreateDayworkResponseDto;
import com.whatachad.app.model.response.Re_CreateDayworkResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Re_Schedule API", description = "스케줄 관련 기능")
@RequestMapping("/v1/re_schedule")
public interface Re_ScheduleCrudApi {
    @Operation(summary = "일정(daywork) 등록",
            description = " daywork 등록 시 기존에 Schedule이 존재하지 않으면 새로 생성하여 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = Re_CreateDayworkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping("/{YYYYMM}/dayworks/{DD}")
    public ResponseEntity<Re_CreateDayworkResponseDto> registerDaywork(@RequestBody Re_CreateDayworkRequestDto requestDto, @PathVariable("YYYYMM") String yearAndMonth, @PathVariable("DD") Integer date);
}
