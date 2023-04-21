package com.whatachad.app.model.dto;


import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DayworkDto {

    private Long id;
    private String title;
    private DayworkPriority priority;
    private Workcheck status;
}
