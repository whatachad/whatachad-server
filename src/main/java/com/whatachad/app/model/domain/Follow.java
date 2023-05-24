package com.whatachad.app.model.domain;

import com.whatachad.app.model.vo.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Follow extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "FOLLOW_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWER")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User follower;

    @Column(name = "FOLLOWING")
    private String followingId;

    public static Follow create(User loginUser, String followingId){
        return Follow.builder()
                .follower(loginUser)
                .followingId(followingId)
                .build();
    }
}
