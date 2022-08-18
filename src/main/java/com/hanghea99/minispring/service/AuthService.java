package com.hanghea99.minispring.service;



import com.hanghea99.minispring.model.Member;
import com.hanghea99.minispring.model.RefreshToken;
import com.hanghea99.minispring.model.dto.*;
import com.hanghea99.minispring.repository.MemberRepository;
import com.hanghea99.minispring.repository.RefreshTokenRepository;
import com.hanghea99.minispring.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//**
@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public boolean signup(MemberRequestDto memberRequestDto) {
		if (memberRepository.existsByUsername(memberRequestDto.getUsername())) {
			return false;
		}

		Member member = memberRequestDto.toMember(passwordEncoder);
		MemberResponseDto.of(memberRepository.save(member));
		return true;
	}

	public Boolean check(UsernameDto usernameDto) {
		return !memberRepository.existsByUsername(usernameDto.getUsername());
	}

	@Transactional
	public TokenDto login(MemberRequestDto memberRequestDto) {
		UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

		RefreshToken refreshToken = RefreshToken.builder()
				.key(authentication.getName())
				.value(tokenDto.getRefreshToken())
				.build();

		refreshTokenRepository.save(refreshToken);

		return tokenDto;
	}

	@Transactional
	public TokenDto reissue(TokenRequestDto tokenRequestDto) {
		if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new IllegalArgumentException("Refresh Token 이 유효하지 않습니다.");
		}

		Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

		RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
				.orElseThrow(() -> new IllegalArgumentException("로그아웃 된 사용자입니다."));

		if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
			throw new IllegalArgumentException("토큰의 유저 정보가 일치하지 않습니다.");
		}

		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

		RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
		refreshTokenRepository.save(newRefreshToken);

		return tokenDto;
	}


}