package com.hanghea99.minispring.controller;


import com.hanghea99.minispring.model.dto.MemberResponseDto;
import com.hanghea99.minispring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//**
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000","https://error-project.vercel.app"}, exposedHeaders = "*", allowedHeaders = "*")
@RequestMapping("/member")
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/me")
	public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
		return ResponseEntity.ok(memberService.getMyInfo());
	}

	@GetMapping("/{username}")
	public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable String username) {
		return ResponseEntity.ok(memberService.getMemberInfo(username));
	}
}
