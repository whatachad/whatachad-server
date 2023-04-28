package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.FacilityResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * package-private
 * (주의) FacilityMapper는 FacilityConverter를 통해 사용해야 한다.
 */
@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface FacilityMapper {

    FacilityMapper facilityMapper = Mappers.getMapper(FacilityMapper.class);

    FacilityDto preprocess(CreateFacilityRequestDto dto);
    FacilityDto preprocess(UpdateFacilityRequestDto dto);
    FacilityResponseDto toResponseDto(Facility entity);

}
