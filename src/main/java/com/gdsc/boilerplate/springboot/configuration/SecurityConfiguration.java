package com.gdsc.boilerplate.springboot.configuration;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gdsc.boilerplate.springboot.security.jwt.JwtAuthenticationEntryPoint;
import com.gdsc.boilerplate.springboot.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final JwtAuthenticationEntryPoint unauthorizedHandler;

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		//@formatter:off

	    return http.cors().and().csrf(csrf -> csrf.disable()) // Enable CORS and disable CSRF
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT authentication filter
	            .authorizeRequests(requests -> requests
	                    .antMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/api-docs", "/actuator/**").permitAll() // Allow unauthenticated access to specific endpoints
	                    .antMatchers(HttpMethod.GET, "/users").permitAll() // Allow unauthenticated GET requests to /users
	                    .antMatchers(HttpMethod.DELETE, "/users").hasAuthority("ADMIN") // Require ADMIN authority for DELETE requests to /users
	                    .anyRequest().authenticated()) // Require authentication for any other requests
	            .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler)) // Configure custom unauthorized handler
	            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configure stateless session management
	            .build(); // Build the security filter chain

		//@formatter:on
	}
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();

	    configuration.setAllowCredentials(false);
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'
	    configuration.setAllowedOrigins(Arrays.asList("*"));
	    configuration.setAllowedMethods(Arrays.asList("*"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

}
