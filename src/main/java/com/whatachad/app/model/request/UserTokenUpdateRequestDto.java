package com.whatachad.app.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserTokenUpdateRequestDto {

	@Schema(title = "jwt token", description = "설명", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank
	private String refreshToken;
}
