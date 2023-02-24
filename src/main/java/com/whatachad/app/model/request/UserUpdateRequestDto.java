package com.whatachad.app.model.request;

import com.whatachad.app.type.UserMetaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateRequestDto {

	@Schema(title = "유저 이메일", description = "설명" , example = "admin123@gmail.com", type = "String")
	@NotBlank
	@Email
	private String email;

	@Schema(title = "기존 비밀번호", description = "설명" , example = "admin1234", type = "String")
	@NotBlank
	// TODO : validation을 어디서 처리할 것인가
//	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#!$%^&+=])(?=\\S+$).{6,16}$", message = "비밀번호 규칙과 동일하지 않습니다.")
	private String password;

	@Schema(title = "새 비밀번호", description = "설명" , example = "admin1234", type = "String")
	@NotBlank
	private String newPassword;

	@Schema(title = "유저 전화번호", description = "설명" , example = "01012345678", type = "String")
	@NotBlank
	// TODO : validation을 어디서 처리할 것인가
//	@Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})")
	private String phone;

	@Schema(title = "유저 속성 추가", description = "필수가 아닌 속성들",
			example = "{'ADDITIONAL_FIELD' : 'VALUE'}", type = "Map<String, String>")
	private Map<UserMetaType, String> meta;
}
