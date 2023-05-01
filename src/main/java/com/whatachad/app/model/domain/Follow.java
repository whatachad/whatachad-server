package com.whatachad.app.model.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Follow extends BaseTime{
    @Id @GeneratedValue
    @Column(name = "FOLLOW_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWER")
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