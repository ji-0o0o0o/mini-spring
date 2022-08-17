package com.hanghea99.minispring.controller;


import com.hanghea99.minispring.model.dto.*;
import com.hanghea99.minispring.service.AuthService;
import com.hanghea99.minispring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//**
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"https://error-project.vercel.app","http://localhost:3000"}, exposedHeaders = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final MemberService memberService;

	@PostMapping("/signup")
	public boolean signup(@RequestBody MemberRequestDto memberRequestDto) {
		return authService.signup(memberRequestDto);
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
		return "환영합니다." + memberRequestDto.getUsername() + "님";
	}

	@GetMapping("test")
	public String test(HttpServletRequest httpServletRequest){

		System.out.println("------------------Authorization------------------");
		System.out.println(httpServletRequest.getHeader("Authorization"));
		System.out.println("---------------------유저정보---------------------");
		System.out.println(memberService.getSigningUser().getId());
		System.out.println(memberService.getSigningUser().getUsername());

		return "안녕하세연 ㅎㅎ";
	}

	@PostMapping ("tests")
	public String tests(HttpServletRequest httpServletRequest){

		System.out.println("------------------Authorization------------------");
		System.out.println(httpServletRequest.getHeader("Authorization"));
		System.out.println("---------------------유저정보---------------------");
		System.out.println(memberService.getSigningUser().getId());
		System.out.println(memberService.getSigningUser().getUsername());

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
