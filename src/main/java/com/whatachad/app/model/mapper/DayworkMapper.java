package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.Daywork;
import com.whatachad.app.model.dto.DayworkDto;
import com.whatachad.app.model.request.CreateDayworkRequestDto;
import com.whatachad.app.model.request.UpdateDayworkRequestDto;
import com.whatachad.app.model.response.DayworkResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface DayworkMapper {

    DayworkMapper dayworkMapper = Mappers.getMapper(DayworkMapper.class);

    DayworkDto toDayworkDto(CreateDayworkRequestDto dto);
    DayworkDto toDayworkDto(UpdateDayworkRequestDto dto);
    DayworkResponseDto toDayworkResponseDto(Daywork daywork);

}
