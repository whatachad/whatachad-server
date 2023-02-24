package com.whatachad.app.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserLoginRequestDto {

	@Schema(title = "유저 아이디", description = "설명" , example = "whatachad123", type = "String")
	@NotBlank
	private String id;

	@Schema(title = "유저 비밀번호", description = "설명" , example = "admin1234", type = "String")
	@NotBlank
	private String password;
}
