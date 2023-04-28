package com.whatachad.app.model.domain;

import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.type.FacilityType;
import com.whatachad.app.util.EntityUtils;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Facility extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private FacilityType category;

    private String title;

    private String description;

    public static Facility create(User user, FacilityDto facilityDto) {
        return Facility.builder()
                .user(user)
                .address(facilityDto.getAddress())
                .category(facilityDto.getCategory())
                .title(facilityDto.getTitle())
                .description(facilityDto.getDescription())
                .build();
    }

    public void update(FacilityDto facilityDto) {
        EntityUtils.setValueExceptNull(this, facilityDto);
    }
}
