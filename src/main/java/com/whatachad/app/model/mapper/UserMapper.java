package com.whatachad.app.model.mapper;

import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.request.SignUpRequestDto;
import com.whatachad.app.model.request.UserUpdateRequestDto;
import com.whatachad.app.model.response.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
		uses = UserQualifier.class,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	@Mapping(source = "password", target = "password", qualifiedByName = {"EncodePassword"})
	User toEntity(SignUpRequestDto dto);

	@Mapping(source = "password", target = "password", qualifiedByName = {"EncodePassword"})
	User toEntity(UserUpdateRequestDto dto);

	UserResponseDto toDto(User entity);
}

