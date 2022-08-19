package com.hanghea99.minispring.model.dto;


import com.hanghea99.minispring.model.Authority;
import com.hanghea99.minispring.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;


//**
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

	private String username;
	private String password;
	private String password2;


	public Member toMember(PasswordEncoder passwordEncoder) {
		return Member.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.authority(Authority.ROLE_USER)
				.build();
	}

	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(username, password);
	}
}
