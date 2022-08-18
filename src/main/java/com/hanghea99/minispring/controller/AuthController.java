package com.hanghea99.minispring.controller;


import com.hanghea99.minispring.model.dto.MemberRequestDto;
import com.hanghea99.minispring.model.dto.TokenDto;
import com.hanghea99.minispring.model.dto.TokenRequestDto;
import com.hanghea99.minispring.model.dto.UsernameDto;
import com.hanghea99.minispring.model.isloginDto;
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
@CrossOrigin(origins = "*", exposedHeaders = "*", allowedHeaders = "*")
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

	@GetMapping("islogin")
	public isloginDto username(HttpServletRequest httpServletRequest){
		isloginDto isloginDto = new isloginDto();
		System.out.println(httpServletRequest.getHeader("Authorization"));
		if (httpServletRequest.getHeader("Authorization") != null){
			isloginDto.setUserName(memberService.getSigningUser().getUsername());
			isloginDto.setIsLogin(true);
			return isloginDto;
		}else {
			return isloginDto;
		}
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
