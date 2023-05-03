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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    public List<User> getFollowoings() {
        return userService.getFollowingUser();
    }

    @Transactional
    public Map<User, DaySchedule> getTodayStatusOfFollowings() {
        LocalDate today = LocalDate.now();

        // 오늘에 대한 DaySchedule이 있는 Schedule만 남김
        List<Schedule> followingSchedule = scheduleService.findFollowingSchedule();
        List<Schedule> todayFollowingSchedule =
                followingSchedule.stream()
                .filter(schedule ->
                        schedule.getDaySchedules()
                                .stream()
                                .filter(daySchedule ->
                                        daySchedule.getDay().equals(today.getDayOfWeek().getValue()))
                                .findFirst().isPresent()
                )
                .toList();

        Map<User, DaySchedule> todayUsersDaySchedule = new HashMap<>();
        todayFollowingSchedule.stream()
                .forEach(schedule ->
                        todayUsersDaySchedule.put(
                                schedule.getUser(),
                                schedule.getDaySchedules().get(0)
                        )
                );
        return todayUsersDaySchedule;
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
