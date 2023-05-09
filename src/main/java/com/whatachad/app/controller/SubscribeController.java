package com.whatachad.app.controller;

import com.whatachad.app.api.SubscribeApi;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Follow;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.mapper.SubscribeConverter;
import com.whatachad.app.model.response.FollowingsResponseDto;
import com.whatachad.app.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubscribeController implements SubscribeApi {

    private final SubscribeService subscribeService;
    private final SubscribeConverter subscribeConverter;

    @Override
    public ResponseEntity<String> follow(String followingId) {
        Follow follow = subscribeService.createFollow(followingId);
        return ResponseEntity.ok().body("SUCCESS");
    }

    @Override
    public ResponseEntity<String> unFollow(String followingId) {
        subscribeService.deleteFollow(followingId);
        return ResponseEntity.ok().body("SUCCESS");
    }

    @Override
    public ResponseEntity<List<FollowingsResponseDto>> getFollowings() {
        List<User> followings = subscribeService.getFollowingUsers();
        Map<String, DaySchedule> followingsAndTodayStatus = subscribeService.getFollowingsAndTodayStatus(toFollowingIds(followings));
        List<FollowingsResponseDto> followingsResponseDtos = subscribeConverter.toFollowingsResponseDtos(followings, followingsAndTodayStatus);
        return ResponseEntity.ok(followingsResponseDtos);
    }

    private List<String> toFollowingIds(List<User> followings) {
        return followings.stream()
                .map(user -> user.getId())
                .toList();
    }
}
