package com.whatachad.app.model.response;

import com.whatachad.app.type.UserMetaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserTokenResponseDto {

	private String accessToken;
	private String refreshToken;

	// 만일 필요한 정보가 있다면 여기 담아서 전달한다.
	@Schema(hidden = true)
	private Map<UserMetaType, String> meta;
}
