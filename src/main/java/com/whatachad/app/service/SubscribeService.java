package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Follow;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final UserService userService;
    private final SubscribeRepository subscribeRepository;

    @Transactional
    public Follow createFollow(String followingId) {
        User follower = getLoginUser();
        if(existFollow(follower.getId(), followingId)) throw new CommonException(BError.EXIST, "Follow Relation");
        return subscribeRepository.save(Follow.create(follower, followingId));
    }

    @Transactional
    public boolean existFollow(String followId, String followingId){
        log.info("신청ID: {}, 로그인아이디: {}", followId, followingId);
        boolean b = subscribeRepository.existsFollow(followId, followingId);
        return b;
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }
}
