package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.DayScheduleDto;
import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DaySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAY_SCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    private Integer date;

    @Enumerated(EnumType.STRING)
    private Workcheck totalDayworkStatus;

    @OneToMany(mappedBy = "daySchedule")
    private List<Daywork> dayworks = new ArrayList<>();

    @OneToMany(mappedBy = "daySchedule")
    private List<Account> accounts = new ArrayList<>();

    public static DaySchedule createByDate(Integer date) {
        return DaySchedule.builder()
                .dayworks(new ArrayList<>())
                .accounts(new ArrayList<>())
                .build();
    }
    public static DaySchedule create(DayScheduleDto dto) {
        return DaySchedule.builder()
                .date(dto.getDate())
                .build();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addDaywork(Daywork daywork) {
        dayworks.add(daywork);
    }

    public Account getLastAccount () {
        int accountSize = this.getAccounts().size();
        return this.getAccounts().get(accountSize - 1);
    }

    @PrePersist
    public void prePersist() {
        this.totalDayworkStatus = Workcheck.NOT_COMPLETE;
    }

}