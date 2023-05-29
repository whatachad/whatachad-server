package com.whatachad.app.api;

import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.AccountResponseDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import com.whatachad.app.model.response.DayworksResponseDto;
import com.whatachad.app.model.response.RecentScheduleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Schedule API", description = "스케줄 관련 기능")
@RequestMapping("/v1/schedule")
@Validated
public interface ScheduleCrudApi {

    @Operation(summary = "일정(daywork) 등록",
            description = "daywork 등록 시 기존에 Schedule이 존재하지 않으면 새로 생성하여 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = DayworkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping("/{YYYYMM}/dayworks/{DD}")
    ResponseEntity<DayworkResponseDto> registerDaywork(@Valid @RequestBody CreateDayworkRequestDto requestDto,
                                                       @PathVariable("YYYYMM")
                                                       @Pattern(regexp = "[0-9]{6}",
                                                               message = "{pattern.year_and_month}")
                                                       String yearAndMonth,
                                                       @PathVariable("DD") Integer date);

    @Operation(summary = "일정(daywork) 수정",
            description = "일정의 title, priority, status, hour, minute 를 수정할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = DayworkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping("/{YYYYMM}/dayworks/{DD}/{dayworkId}")
    ResponseEntity<DayworkResponseDto> editDaywork(@Valid @RequestBody UpdateDayworkRequestDto requestDto,
                                                   @PathVariable Long dayworkId);

    @Operation(summary = "일정(daywork) 삭제",
            description = "daywork_id를 이용해 일정을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/{YYYYMM}/dayworks/{DD}/{dayworkId}")
    void deleteDaywork(@PathVariable Long dayworkId);

    @Operation(summary = "가계부 등록",
            description = "가계부 등록 시 기존에 Schedule이 존재하지 않으면 새로 생성하여 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping("/{YYYYMM}/accounts/{DD}")
    ResponseEntity<AccountResponseDto> registerAccount(@RequestBody @Valid CreateAccountRequestDto requestDto,
                                                       @PathVariable("YYYYMM")
                                                       @Pattern(regexp = "[0-9]{6}",
                                                               message = "{account.year_and_month.pattern}")
                                                               String yearAndMonth,
                                                       @PathVariable("DD") Integer day);


    @Operation(summary = "가계부 수정",
            description = "가계부의 title, cost, type, category, hour, minute 를 수정할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AccountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping("/{YYYYMM}/accounts/{DD}/{accountId}")
    ResponseEntity<AccountResponseDto> editAccount(@RequestBody @Validated UpdateAccountRequestDto requestDto,
                                                   @PathVariable Long accountId);


    @Operation(summary = "가계부 삭제",
            description = "account_id를 이용해 가계부를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/{YYYYMM}/accounts/{DD}/{accountId}")
    void deleteAccount(@PathVariable Long accountId);

    @Operation(summary = "캘린더 조회",
            description = "year 과 month 정보를 이용하여 캘린더를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = DayworksResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping("/{YYYYMM}")
    ResponseEntity<List<DayworksResponseDto>> getSchedule(@PathVariable("YYYYMM") String yearAndMonth);


    @Operation(summary = "최근 내역 조회",
            description = "year 과 month 정보를 이용하여 최근 내역을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = ScheduleRecentResponseSlice.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping({"/{YYYYMM}/recent/{page}", "/{YYYYMM}/recent"})
    ResponseEntity<Slice<RecentScheduleResponseDto>> getRecentSchedule(@PathVariable("YYYYMM") String yearAndMonth,
                                                                       @PathVariable(required = false) Integer page);

    class ScheduleRecentResponseSlice extends SliceImpl<RecentScheduleResponseDto> {
        public ScheduleRecentResponseSlice(List<RecentScheduleResponseDto> content, Pageable pageable, boolean hasNext) {
            super(content, pageable, hasNext);
        }
    }
}

