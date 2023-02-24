package com.whatachad.app.api;

import com.whatachad.app.dto.ScheduleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Tag(name = "Schedule API", description = "스케줄 관련 기능")
@RequestMapping("/v1/schedule/")
public interface ScheduleApi {

    @Operation(summary = "스케줄 조회", description = "해당 유저의 오늘 스케줄 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ScheduleDto.class))),
            @ApiResponse(responseCode = "405", description = "Invalid input")
    })
    @GetMapping
    ResponseEntity<ScheduleDto> getSchedule();

}
