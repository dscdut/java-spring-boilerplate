package com.gdsc.boilerplate.springboot.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gdsc.boilerplate.springboot.security.dto.UserPrinciple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

	private final JwtProperties jwtProperties;

	public String generateToken(UserPrinciple userPrinciple) {

		final Long userId = userPrinciple.getId();

		//@formatter:off
		return JWT.create()
				.withSubject(Long.toString(userId))
				.withIssuer(jwtProperties.getIssuer())
//				.withClaim("roles", String.join(";", (CharSequence[]) userPrinciple.getAuthorities().toArray()))
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMinute() * 60 * 1000))
				.sign(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes()));
		//@formatter:on
	}

	public Long getUserIdFromToken(String token) {

		final DecodedJWT decodedJWT = getDecodedJWT(token);

		return Long.parseLong(decodedJWT.getSubject());
	}
	
	public boolean validateToken(String token, Long userId) {

		final Long userIdFromToken = getUserIdFromToken(token);

		final boolean equalsUsername = userIdFromToken.equals(userId);
		final boolean tokenExpired = isTokenExpired(token);

		return equalsUsername && !tokenExpired;
	}

    
	private boolean isTokenExpired(String token) {

		final Date expirationDateFromToken = getExpirationDateFromToken(token);
		return expirationDateFromToken.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {

		final DecodedJWT decodedJWT = getDecodedJWT(token);

		return decodedJWT.getExpiresAt();
	}

	private DecodedJWT getDecodedJWT(String token) {

		final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes())).build();

		return jwtVerifier.verify(token);
	}

}
