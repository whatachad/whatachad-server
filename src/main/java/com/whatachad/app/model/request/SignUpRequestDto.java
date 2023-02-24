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

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

@Getter
@Setter
@AllArgsConstructor
public class SignUpRequestDto {

	@Schema(title = "유저 아이디", description = "설명" , example = "admin")
	@NotBlank
	private String id;

	@Schema(title = "유저 이메일", description = "설명" , example = "admin123@gmail.com")
	@NotBlank
	@Email
	private String email;

	@Schema(title = "유저 비밀번호", description = "설명" , example = "admin", requiredMode = REQUIRED)
	@NotBlank
//	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#!$%^&+=])(?=\\S+$).{6,16}$", message = "비밀번호 규칙과 동일하지 않습니다.")
	private String password;

	@Schema(title = "유저 이름", description = "설명" , example = "whatachad", requiredMode = REQUIRED)
	@NotBlank
	private String name;

	@Schema(title = "유저 전화번호", description = "설명" , example = "01012345678", requiredMode = REQUIRED)
	@NotBlank
//	@Pattern(regexp = "(\\d{3})-(\\d{3,4})-(\\d{4})")
//	@Pattern(regexp = "(\\d{3})(\\d{3,4})(\\d{4})")
	private String phone;

	@Schema(title = "유저 속성 추가", description = "필수가 아닌 속성들",
			example = "{\"ROLE\" : \"ROLE_ADMIN\"}")
	private Map<UserMetaType, String> meta;
}
