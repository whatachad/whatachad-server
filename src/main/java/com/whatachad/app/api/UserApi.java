package com.whatachad.app.api;

import com.whatachad.app.model.request.SignUpRequestDto;
import com.whatachad.app.model.request.UserLoginRequestDto;
import com.whatachad.app.model.request.UserTokenUpdateRequestDto;
import com.whatachad.app.model.request.UserUpdateRequestDto;
import com.whatachad.app.model.response.UserResponseDto;
import com.whatachad.app.model.response.UserTokenResponseDto;
import com.whatachad.app.security.AuthConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User API", description = "유저 관련 기능")
@Validated
public interface UserApi {

    @Operation(summary = "유저 로그인", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping(value = "/v1/login")
    ResponseEntity<UserTokenResponseDto> login(@Valid @RequestBody UserLoginRequestDto loginDTO);



    @Operation(summary = "유저 로그아웃", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping("/v1/logout")
    ResponseEntity logout(@RequestHeader(value = AuthConstant.AUTHORIZATION) String token);



    @Operation(summary = "회원가입", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping(path = "/v1/signUp", consumes = "application/json")
    ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto);



    @Operation(summary = "메일 인증 API", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping(path = "/v1/signUpConfirm")
    String signUpConfirm(@RequestParam("email") String email, @RequestParam("authKey") String authKey);



    @Operation(summary = "유저 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping("/v1/users")
    ResponseEntity<List<UserResponseDto>> getUserList();



    @Operation(summary = "유저 정보 수정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping(path = "/v1/users", consumes = "application/json")
    ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto);



    @Operation(summary = "Token Update API", description = "기존 Token을 받아서 (refresh가 유효할 경우) 새로운 Token으로 갱신한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PutMapping("/v1/token")
    ResponseEntity<UserTokenResponseDto> updateAccessToken(
            @Valid @RequestBody UserTokenUpdateRequestDto userTokenUpdateRequestDto);



    @Operation(summary = "유저 삭제", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping(path = "/v1/users/{id}")
    ResponseEntity<UserResponseDto> deleteUser(@PathVariable("id") String id);
}
