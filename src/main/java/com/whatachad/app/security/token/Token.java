package com.whatachad.app.security.token;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Token {

    String accessToken;
    String refreshToken;

    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
