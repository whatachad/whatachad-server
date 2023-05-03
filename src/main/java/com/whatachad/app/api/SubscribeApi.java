package com.whatachad.app.api;

import com.whatachad.app.model.response.FollowingsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subscribe API", description = "구독 관련 기능")
@RequestMapping("/v1/subscribe")
public interface SubscribeApi {

    @Operation(summary = "follow",
            description = "follow - following 관계 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @PostMapping("/follow/{followingId}")
    ResponseEntity<String> follow(@PathVariable String followingId);


    @Operation(summary = "unfollow",
            description = "follow - following 관계 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/unfollow/{followingId}")
    ResponseEntity<String> unFollow(@PathVariable String followingId);


    @Operation(summary = "following 목록 가져오기",
            description = "현재 로그인한 유저의 following 목록을 가져온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping("/followings")
    ResponseEntity<List<FollowingsResponseDto>> getFollowings();
}
