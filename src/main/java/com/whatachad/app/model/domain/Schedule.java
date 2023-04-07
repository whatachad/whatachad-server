package com.whatachad.app.model.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Schedule{

    @Id @GeneratedValue
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private int year;
    private int month;
    private int budget;

    @Builder.Default
    @OneToMany(mappedBy = "schedule")
    private List<Daywork> dayworks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "schedule")
    private List<Account> accounts = new ArrayList<>();
}
