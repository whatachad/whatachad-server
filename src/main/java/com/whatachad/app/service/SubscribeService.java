package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.Follow;
import com.whatachad.app.model.domain.Schedule;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final UserService userService;
    private final SubscribeRepository subscribeRepository;
    private final ScheduleService scheduleService;

    @Transactional
    public Follow createFollow(String followingId) {
        User follower = getLoginUser();
        if (existFollow(follower.getId(), followingId)) throw new CommonException(BError.EXIST, "Follow Relation");
        return subscribeRepository.save(Follow.create(follower, followingId));
    }

    @Transactional
    public void deleteFollow(String followingId) {
        User follower = getLoginUser();
        Follow findFollow = subscribeRepository.findByFollow(follower.getId(), followingId)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Follow"));
        subscribeRepository.delete(findFollow);
    }

    @Transactional(readOnly = true)
    public List<User> getFollowingUsers() {
        return userService.getFollowingUser();
    }


    @Transactional
    public Map<String, DaySchedule> getFollowingsAndTodayStatus(List<String> followingsIds) {
        List<Schedule> followingsTodaySchedules = scheduleService.findFollowingTodaySchedule(followingsIds);
        return followingsTodaySchedules.stream()
                .collect(Collectors.toMap(
                        schedule -> schedule.getUser().getId(),
                        schedule -> schedule.getDaySchedules().get(0)));
    }

    /**
     * private Method
     */
    @Transactional
    private boolean existFollow(String followId, String followingId) {
        return subscribeRepository.existsFollow(followId, followingId);
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        return userService.getUser(loginUserId);
    }
}
