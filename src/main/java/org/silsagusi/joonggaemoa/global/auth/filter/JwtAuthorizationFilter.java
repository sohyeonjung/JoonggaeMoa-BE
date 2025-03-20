package org.silsagusi.joonggaemoa.global.auth.filter;

import java.io.IOException;
import java.util.Collections;

import org.silsagusi.joonggaemoa.global.auth.jwt.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		final String token = request.getHeader("Authorization");

		String username = null;
		String roles = null;

		if (token != null && !token.isEmpty()) {
			String jwtToken = token.substring(7);

			Claims claims = jwtProvider.getClaims(jwtToken);
			username = claims.get("username", String.class);
			roles = claims.get("roles", String.class);
		}

		if (username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
			SecurityContextHolder.getContext().setAuthentication(getUserAuth(username, roles));
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
