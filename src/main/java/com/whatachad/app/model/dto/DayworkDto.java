package com.whatachad.app.model.dto;


import com.whatachad.app.type.DayworkPriority;
import com.whatachad.app.type.Workcheck;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class DayworkDto {

    private Long id;
    private LocalDate dayworkDate;
    private String title;
    private DayworkPriority priority;
    private Workcheck status;
}
