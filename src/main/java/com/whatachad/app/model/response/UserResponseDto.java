package com.whatachad.app.model.response;

import com.whatachad.app.type.UserMetaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {

	@Schema(title = "유저 아이디", description = "설명" , example = "whatachad123")
	private String id;

	@Schema(title = "유저 이메일", description = "설명" , example = "admin123@gmail.com")
	@NotBlank
	@Email
	private String email;

	@Schema(title = "유저 이름", description = "설명" , example = "whatachad")
	@NotBlank
	private String name;

	@Schema(title = "유저 전화번호", description = "설명" , example = "010-0000-0000")
	@NotBlank
	// TODO : validation을 어디서 처리할 것인가
//	@Pattern(regexp = "(\\d{3})-(\\d{3,4})-(\\d{4})")
	private String phone;

	@Schema(title = "유저 속성 추가", description = "필수가 아닌 속성들",
			example = "{'ADDITIONAL_FIELD' : 'VALUE'}", type = "Map<String, String>")
	private Map<UserMetaType, String> meta;

	@Schema(hidden = true)
	private LocalDateTime createdAt;

	@Schema(hidden = true)
	private LocalDateTime updatedAt;

}
