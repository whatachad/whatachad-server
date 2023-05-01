package com.whatachad.app.controller;

import com.whatachad.app.api.SubscribeApi;
import com.whatachad.app.model.domain.Follow;
import com.whatachad.app.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

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

}
