package com.whatachad.app.api;

import com.whatachad.app.model.request.CreateAccountRequestDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateAccountRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.CreateAccountResponseDto;
import com.whatachad.app.model.response.CreateDayworkResponseDto;
import com.whatachad.app.model.response.UpdateAccountResponseDto;
import com.whatachad.app.model.response.UpdateDayworkResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule API", description = "스케줄 관련 기능")
@RequestMapping("/v1/schedule")
public interface ScheduleCrudApi {

    @Operation(summary = "일정(daywork) 등록",
            description = " daywork 등록 시 기존에 Schedule이 존재하지 않으면 새로 생성하여 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CreateDayworkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping("/{YYYYMM}/dayworks/{DD}")
    public ResponseEntity<CreateDayworkResponseDto> registerDaywork(@RequestBody CreateDayworkRequestDto requestDto, @PathVariable("YYYYMM") String ym, @PathVariable("DD") Integer date);


    @Operation(summary = "일정(daywork) 수정",
            description = "일정의 title, priority, status, hour, minute 를 수정할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = UpdateDayworkRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping("/{YYYYMM}/dayworks/{DD}/{daywork_id}")
    public ResponseEntity<UpdateDayworkResponseDto> editDaywork(@RequestBody UpdateDayworkRequestDto requestDto, @PathVariable Long daywork_id);


    @Operation(summary = "일정(daywork) 삭제",
            description = "일정의 삭제한다. 일정과 관계된 Schedule에 하위의 것이 존재하지 않으면 같이 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/{YYYYMM}/dayworks/{DD}/{daywork_id}")
    public void deleteDaywork(@PathVariable Long daywork_id);


    @Operation(summary = "가계부 등록",
            description = " 가계부 등록 시 기존에 Schedule이 존재하지 않으면 새로 생성하여 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = CreateAccountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping("/{YYYYMM}/accounts/{DD}")
    public ResponseEntity<CreateAccountResponseDto> registerAccount(@RequestBody CreateAccountRequestDto requestDto, @PathVariable("YYYYMM") String ym, @PathVariable("DD") Integer date);

    @Operation(summary = "가계부 수정",
            description = "가계부의 title, cost, type, category, hour, minute 를 수정할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = UpdateDayworkRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping("/{YYYYMM}/accounts/{DD}/{account_id}")
    public  ResponseEntity<UpdateAccountResponseDto> editAccount(@RequestBody UpdateAccountRequestDto requestDto, @PathVariable Long account_id);


    @Operation(summary = "가계부 삭제",
            description = "가계부를 삭제한다. 가계부과 관계된 Schedule에 하위의 것이 존재하지 않으면 같이 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/{YYYYMM}/accounts/{DD}/{account_id}")
    public void deleteAccount(@PathVariable Long account_id);

}