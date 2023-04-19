package com.whatachad.app.api;

import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import com.whatachad.app.model.response.UpdateDayworkResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Schedule API", description = "스케줄 관련 기능")
@RequestMapping("/v1/schedule")
public interface ScheduleCrudApi {
    @Operation(summary = "일정(daywork) 등록",
            description = " daywork 등록 시 기존에 Schedule이 존재하지 않으면 새로 생성하여 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = DayworkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping("/{YYYYMM}/dayworks/{DD}")
    public ResponseEntity<DayworkResponseDto> registerDaywork(@RequestBody CreateDayworkRequestDto requestDto, @PathVariable("YYYYMM") String yearAndMonth, @PathVariable("DD") Integer date);

    @Operation(summary = "일정(daywork) 수정",
            description = "일정의 title, priority, status, hour, minute 를 수정할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = UpdateDayworkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping("/{YYYYMM}/dayworks/{DD}/{dayworkId}")
    public ResponseEntity<UpdateDayworkResponseDto> editDaywork(@RequestBody UpdateDayworkRequestDto requestDto, @PathVariable Long dayworkId);

    @Operation(summary = "일정(daywork) 삭제",
            description = "daywork_id를 이용해 일정을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/{YYYYMM}/dayworks/{DD}/{dayworkId}")
    public void deleteDaywork(@PathVariable Long dayworkId);

    @Operation(summary = "캘린더 조회",
            description = "year 과 month 정보를 이용하여 캘린더를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping("/{YYYYMM}")
    public ResponseEntity<Map<String, Object>> getSchedule(@PathVariable("YYYYMM") String yearAndMonth);
}
