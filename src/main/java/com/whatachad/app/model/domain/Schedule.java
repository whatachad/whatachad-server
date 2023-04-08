package com.whatachad.app.model.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule{

    @Id @GeneratedValue
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private Integer year;
    private Integer month;
    private Integer budget;

    @Builder.Default
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Daywork> dayworks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();
}