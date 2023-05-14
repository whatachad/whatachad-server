package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.DaySchedule;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.response.FollowingsResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class SubscribeConverter {

    public List<FollowingsResponseDto> toFollowingsResponseDtos(List<User> followings, Map<String, DaySchedule> followingsAndTodayStatus) {
        int day = LocalDate.now().getDayOfMonth();
        return followings.stream()
                .map(following -> new FollowingsResponseDto(following.getId(),
                        following.getName(),
                        followingsAndTodayStatus.getOrDefault(following.getId(), DaySchedule.create(day)).getTotalDayworkStatus())
                ).toList();
    }
}
