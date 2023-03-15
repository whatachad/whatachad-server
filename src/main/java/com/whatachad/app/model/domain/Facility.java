package com.whatachad.app.model.domain;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.common.IError;
import com.whatachad.app.model.dto.CreateFacilityDto;
import com.whatachad.app.model.dto.UpdateFacilityDto;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.lang.reflect.Field;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Facility extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    public static Facility create(CreateFacilityDto dto) {
        return Facility.builder()
                .address(dto.getAddress())
                .build();
    }

    /**
     * <변경된 필드만 업데이트하는 메서드>
     * 주의) 엔티티에서 기존의 필드명을 수정하거나 새로운 필드가 추가된다면
     * 반드시 아래 메서드에 변경 사항을 반영해야 한다.
     * @param dto
     */
    public void update(UpdateFacilityDto dto) {
        UpdateUtils.changeField(this, UpdateUtils.FACILITY_ADDRESS, dto.getAddress());
    }
}
