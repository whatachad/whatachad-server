package com.whatachad.app.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginRequestDto {

	@Schema(title = "유저 아이디", description = "설명" , example = "admin", type = "String")
	@NotBlank
	private String id;

	@Schema(title = "유저 비밀번호", description = "설명" , example = "admin", type = "String")
	@NotBlank
	private String password;
}
