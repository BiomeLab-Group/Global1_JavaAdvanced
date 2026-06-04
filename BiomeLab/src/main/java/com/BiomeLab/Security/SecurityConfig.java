package com.BiomeLab.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JWTAuthFilter authFilter;
	
	@Bean
	public SecurityFilterChain filtrar(HttpSecurity request) throws Exception {
		
		request
			.csrf(csrf -> csrf.disable())
			.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.authorizeHttpRequests(req -> 
			req.requestMatchers("/autenticacao/**","/swagger-ui/**","/v3/**","/h2-console/**").permitAll()
								.anyRequest().authenticated())
			.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			;
		
		return request.build();
	}

}
