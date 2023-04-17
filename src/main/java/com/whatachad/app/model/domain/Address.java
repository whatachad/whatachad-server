package com.whatachad.app.model.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String roadAddress;
    private String jibunAddress;
    private String regionCode;
    private Double latitude;
    private Double longitude;

}
