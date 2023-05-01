package com.whatachad.app.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @PostMapping("/{followingId}")
    ResponseEntity<String> follow(@PathVariable String followingId);
    
    @Operation(summary = "unfollow",
            description = "follow - following 관계 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @DeleteMapping("/{followingId}")
    ResponseEntity<String> unFollow(@PathVariable String followingId);

}
