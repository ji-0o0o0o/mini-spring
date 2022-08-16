package com.hanghea99.minispring.security;



import com.hanghea99.minispring.security.jwt.JwtAccessDeniedHandler;
import com.hanghea99.minispring.security.jwt.JwtAuthenticationEntryPoint;
import com.hanghea99.minispring.security.jwt.JwtSecurityConfig;
import com.hanghea99.minispring.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//**
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity webSecurity) {
		webSecurity.ignoring()
				.antMatchers("/h2-console/**", "/favicon.ico"); // h2콘솔 == 열어주겠다. 보안해제
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()

				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				.and()
				.headers()
				.frameOptions()
				.sameOrigin()

				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()
				.authorizeRequests()
				.antMatchers("/api/signup").permitAll() //permitAll = 열어주겠다.
				.antMatchers("/api/signup/check").permitAll()
				.antMatchers("/api/login").permitAll()
				.antMatchers("/api/logout").permitAll()
				.antMatchers("/api/test").permitAll()
				.antMatchers("/api/**").permitAll()
				.anyRequest().authenticated()// authenticated = 닫겠다.

				.and()
				.apply(new JwtSecurityConfig(tokenProvider));
	}
}
