package com.whatachad.app.filter;


import com.whatachad.app.security.AuthConstant;
import com.whatachad.app.service.TokenService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
		throws ServletException, IOException {

		try {
			if (checkJWTToken(request, response)) { // JWT 토큰을 체크
				Claims claims = tokenService.validateToken(
					request.getHeader(AuthConstant.AUTHORIZATION)
						.replace(AuthConstant.BEARER, ""));

				if (tokenService.getUserCode(claims.getSubject()) == (int)claims.get("code")
					&& claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				}
			} else if (Objects.nonNull(request.getHeader(AuthConstant.AUTHORIZATION)) // 디버그 모드인 경우
				&& request.getHeader(AuthConstant.AUTHORIZATION).equals(AuthConstant.DEBUG_MODE)) {

				SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken("admin", null,
						AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")));
			} else {
				SecurityContextHolder.clearContext();
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
			SecurityContextHolder.clearContext();
			filterChain.doFilter(request, response);
			return;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List)claims.get("authorities");
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
			authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse response) {
		String authenticationHeader = request.getHeader(AuthConstant.AUTHORIZATION);
		if (authenticationHeader == null || !authenticationHeader.startsWith(AuthConstant.BEARER))
			return false;
		return true;
	}
}
