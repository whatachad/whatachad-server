package com.whatachad.app.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AreaRequestDto {

    private static final String DELIMITER = " ";

    @NotBlank
    private String l1;
    private String l2;
    private String l3;

    public static String getArea(String l1, String l2, String l3) {
        StringJoiner area = new StringJoiner(DELIMITER);
        if (l1 != null) area.add(l1);
        if (l2 != null) area.add(l2);
        if (l3 != null) area.add(l3);
        return String.valueOf(area);
    }

    public String getArea() {
        StringJoiner area = new StringJoiner(DELIMITER);
        if (l1 != null) area.add(l1);
        if (l2 != null) area.add(l2);
        if (l3 != null) area.add(l3);
        return String.valueOf(area);
    }
}
