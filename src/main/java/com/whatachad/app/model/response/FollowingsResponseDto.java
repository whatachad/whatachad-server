package com.whatachad.app.model.response;

import com.whatachad.app.type.Workcheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FollowingsResponseDto {
    private String id;
    private String name;
    private Workcheck todayDayworkStatus;
}
