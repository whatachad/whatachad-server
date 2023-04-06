package com.whatachad.app.type;

import java.util.List;

public enum FacilityType {

    // TODO : 대분류 -> 소분류
    HEALTH,
    PILATES,
    CROSSFIT,
    YOGA,
    CLIMBING,
    PULL_UP_BAR,
    PARALLEL_BAR,
    TENNIS,
    TABLE_TENNIS,
    BADMINTON,
    FOOTBALL,
    SOCCER,
    BASEBALL,
    BOWLING,
    BILLIARD,
    GOLF,
    BOXING,
    KICK_BOXING,
    JUJITSU,
    MUAY_THAI,
    MMA,
    ETC;


    public static boolean contains(String value) {
        List<String> allTypeNames = List.of(
                HEALTH.name(),
                PILATES.name(),
                CROSSFIT.name(),
                YOGA.name(),
                CLIMBING.name(),
                PULL_UP_BAR.name(),
                PARALLEL_BAR.name(),
                TENNIS.name(),
                TABLE_TENNIS.name(),
                BADMINTON.name(),
                FOOTBALL.name(),
                SOCCER.name(),
                BASEBALL.name(),
                BOWLING.name(),
                BILLIARD.name(),
                GOLF.name(),
                BOXING.name(),
                KICK_BOXING.name(),
                JUJITSU.name(),
                MUAY_THAI.name(),
                MMA.name(),
                ETC.name());
        return allTypeNames.contains(value);
    }

}
