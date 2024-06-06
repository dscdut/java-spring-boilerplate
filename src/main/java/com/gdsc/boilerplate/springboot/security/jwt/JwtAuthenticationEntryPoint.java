package com.gdsc.boilerplate.springboot.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.boilerplate.springboot.exceptions.ApiExceptionResponse;
import com.gdsc.boilerplate.springboot.exceptions.ExceptionConstants;
import com.gdsc.boilerplate.springboot.utils.ExceptionMessageAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Autowired
	private ExceptionMessageAccessor accessor;
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		final ApiExceptionResponse res = new ApiExceptionResponse(
				ExceptionConstants.UNAUTHORIZED.getCode(),
				accessor.getMessage(null, ExceptionConstants.UNAUTHORIZED.getMessageName()));

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(res);

		response.getWriter().write(jsonResponse);

	}

}
