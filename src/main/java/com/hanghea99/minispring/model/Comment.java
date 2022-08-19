package com.hanghea99.minispring.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghea99.minispring.model.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends Timestamped{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn
	@JsonBackReference
	private Member member;

	@ManyToOne
	@JoinColumn
	@JsonBackReference
	private Article article;

	private int heartCnt;
	private int commentCnt;



	@OneToMany(mappedBy = "comment", orphanRemoval = true)
	@JsonIgnore
	private List<Heart> heartList = new ArrayList<>();

	public Comment(CommentRequestDto commentRequestDto, Article article, Member member) {
		this.username = member.getUsername();
		this.content = commentRequestDto.getComment();
		this.article = article;
		this.member = member;
	}
	public void updateComment(CommentRequestDto commentRequestDto) {
		this.content = commentRequestDto.getComment();
	}


	public void addHeart(Heart heart) {
		this.heartList.add(heart);
	}
	public void removeHeart(Heart heart) {
		this.heartList.remove(heart);
	}
	public void setHeartCnt(int heartListSize) {
		this.heartCnt = heartListSize;
	}


}
