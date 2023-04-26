package com.whatachad.app.model.domain;

import com.whatachad.app.type.Workcheck;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.whatachad.app.util.EntityUtils.*;

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

    private Integer day;

    @Enumerated(EnumType.STRING)
    private Workcheck totalDayworkStatus;

    @OneToMany(mappedBy = "daySchedule")
    private List<Daywork> dayworks;

    @OneToMany(mappedBy = "daySchedule")
    private List<Account> accounts;

    public static DaySchedule create(Integer day) {
        return DaySchedule.builder()
                .day(day)
                .dayworks(new ArrayList<>())
                .accounts(new ArrayList<>())
                .totalDayworkStatus(Workcheck.NOT_COMPLETE)
                .build();
    }

    public void addAccount(Account account) {
        accounts.add(account);
        setEntity("daySchedule", account, this);
    }

    public void addDaywork(Daywork daywork) {
        dayworks.add(daywork);
        setEntity("daySchedule", daywork, this);
    }

    public Account getLastAccount () {
        int lastIndex = this.getAccounts().size() - 1;
        return this.getAccounts().get(lastIndex);
    }

    public Daywork getLastDaywork () {
        int lastIndex = this.getDayworks().size() - 1;
        return this.getDayworks().get(lastIndex);
    }

}