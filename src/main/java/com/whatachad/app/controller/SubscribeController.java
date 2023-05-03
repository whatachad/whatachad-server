package com.whatachad.app.controller;

import com.whatachad.app.api.SubscribeApi;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Follow;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.response.FollowingsResponseDto;
import com.whatachad.app.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubscribeController implements SubscribeApi {

    private final SubscribeService subscribeService;

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
        List<User> followings = subscribeService.getFollowoings();
        Map<User, DaySchedule> todayStatusOfFollowings = subscribeService.getTodayStatusOfFollowings();
        int day = LocalDate.now().getDayOfWeek().getValue();

        List<FollowingsResponseDto> followingsResponseDtos = followings.stream().map(following ->
                new FollowingsResponseDto(following.getId(),
                        following.getName(),
                        todayStatusOfFollowings.getOrDefault(following, DaySchedule.create(day)).getTotalDayworkStatus())
        ).toList();

        return ResponseEntity.ok(followingsResponseDtos);
    }
}
