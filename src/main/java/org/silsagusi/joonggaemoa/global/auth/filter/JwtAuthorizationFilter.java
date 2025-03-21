package org.silsagusi.joonggaemoa.global.auth.filter;

import java.io.IOException;
import java.util.Collections;

import org.silsagusi.joonggaemoa.global.auth.jwt.JwtProvider;
import org.silsagusi.joonggaemoa.global.auth.jwt.RefreshToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		final String token = request.getHeader("Authorization");

		Long id = null;
		String username = null;
		String roles = null;

		if (token != null && !token.isEmpty()) {
			Claims claims = jwtProvider.getClaims(token.substring(7));
			id = Long.valueOf(claims.getId());
			username = claims.get("username", String.class);
			roles = claims.get("roles", String.class);
		}

		if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
			Authentication authentication = getUserAuth(username, roles);

			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (jwtProvider.isTokenExpired(token)) {
				String refreshToken = RefreshToken.getRefreshToken(id);

				if (refreshToken != null) {
					String newAccessToken = jwtProvider.generateAccessToken(authentication);
					String newRefreshToken = jwtProvider.generateRefreshToken(id);

					RefreshToken.removeRefreshToken(id);
					RefreshToken.putRefreshToken(id, newRefreshToken);

					response.addHeader("Authorization", "Bearer " + newAccessToken);
					log.info("Refresh token: " + newAccessToken);
				}
			}
		}

		filterChain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getUserAuth(String username, String roles) {
		return new UsernamePasswordAuthenticationToken(
			username, "",
			Collections.singleton(new SimpleGrantedAuthority(roles))
		);
	}
}
