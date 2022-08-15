package com.hanghea99.minispring.controller;


import com.hanghea99.minispring.model.dto.*;
import com.hanghea99.minispring.service.AuthService;
import com.hanghea99.minispring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//**
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
		return ResponseEntity.ok(authService.signup(memberRequestDto));
	}

	@PostMapping("/signup/check")
	public Boolean check(@RequestBody UsernameDto usernameDto){
		return authService.check(usernameDto);
	}


	@PostMapping("/login")
	public String login(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		TokenDto tokenDto = authService.login(memberRequestDto);
		httpServletResponse.setHeader("Authorization", "Bearer    " + tokenDto.getAccessToken());
//		Cookie jwt = new Cookie("jwt",  tokenDto.getAccessToken());
//		jwt.setMaxAge(1000 * 60 * 60 * 12);
//		httpServletResponse.addCookie(jwt);
		System.out.println("-----------------------토큰이당-------------------------");
		System.out.println(httpServletRequest.getHeader("Authorization"));
		System.out.println("-----------------------위는 어쏠 아래 쿠키-------------------------");
		System.out.println(httpServletRequest.getHeader("Cookie"));
		System.out.println("-----------------------토큰이당-------------------------");


//		Cookie[] cookies = httpServletRequest.getCookies();
//
//		int i = 1;
//		for(Cookie c :cookies){
//			System.out.println(i+"벙");
//			System.out.println(c.getName());
//			System.out.println(c.getValue());
//			i++;
//		}
		
		return "환영합니다." + memberRequestDto.getUsername() + "님";
	}

	@PostMapping("test")
	public String test(HttpServletRequest httpServletRequest){
		System.out.println("-----------------------토큰이당당-------------------------");
		System.out.println(httpServletRequest.getHeader("Authorization"));
		System.out.println("-----------------------위는 어쏠 아래 쿠키-------------------------");
		System.out.println(httpServletRequest.getHeader("Cookie"));
		System.out.println("-----------------------라ㅏ라라라라-------------------------");
		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		System.out.println("제발여 살려주세여");
		System.out.println(name);
		System.out.println(memberService.getSigningUser().getUsername());
//		Cookie[] cookies = httpServletRequest.getCookies();
//		int i = 1;
//		for(Cookie c :cookies){
//			System.out.println(i+"번");
//			System.out.println(c);
//			i++;
//		}
		System.out.println("-----------------------토큰이당당당-------------------------");


		return "안녕하세연 ㅎㅎ";
	}

	@PostMapping("/logout")
	private String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		Cookie jwt = new Cookie("jwt", null);
		jwt.setMaxAge(0);
		httpServletResponse.addCookie(jwt);
		return "로그아웃";
	}

	@PostMapping("/reissue")
	public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
		return ResponseEntity.ok(authService.reissue(tokenRequestDto));
	}
}
