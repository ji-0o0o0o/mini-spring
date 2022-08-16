package com.hanghea99.minispring.model.dto;

import com.hanghea99.minispring.model.Article;
import com.hanghea99.minispring.model.Member;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class memeDto {
	private Article article;
	private List<Member> memberList = new ArrayList<>();

	public memeDto(Article article) {
		this.article = article;
	}

	public void add(Member member){
		this.memberList.add(member);
	}
}
