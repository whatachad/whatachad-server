package com.whatachad.app.model.domain;

import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.type.FacilityType;
import jakarta.persistence.*;
import lombok.*;

import static com.whatachad.app.model.domain.UpdateUtils.*;

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

    public static Facility create(User user, FacilityDto dto) {
        return Facility.builder()
                .user(user)
                .address(dto.getAddress())
                .category(dto.getCategory())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    /**
     * <변경된 필드만 업데이트하는 메서드>
     * 주의) 엔티티에서 기존의 필드명을 수정하거나 새로운 필드가 추가된다면
     * 반드시 아래 메서드에 변경 사항을 반영해야 한다.
     * @param dto
     */
    public void update(FacilityDto dto) {
        changeField(this, FACILITY_TITLE, dto.getTitle());
        changeField(this, FACILITY_ADDRESS, dto.getAddress());
        changeField(this, FACILITY_CATEGORY, dto.getCategory());
        changeField(this, FACILITY_TITLE, dto.getTitle());
        changeField(this, FACILITY_DESCRIPTION, dto.getDescription());
    }
}
