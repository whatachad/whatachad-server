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
    PARALLEL_BAR;

    public static boolean contains(String value) {
        List<String> allTypeNames = List.of(
                HEALTH.name(),
                PILATES.name(),
                CROSSFIT.name(),
                YOGA.name(),
                CLIMBING.name(),
                PULL_UP_BAR.name(),
                PARALLEL_BAR.name());
        return allTypeNames.contains(value);
    }

}
