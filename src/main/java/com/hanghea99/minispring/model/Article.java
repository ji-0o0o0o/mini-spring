package com.hanghea99.minispring.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghea99.minispring.model.dto.ArticleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Article  extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 1000)
	private String content;


	private Boolean isDone = false;

	private String imgUrl;

	private Long selectedCommentId;

	@Enumerated(EnumType.STRING)
	private Language language;

	@JsonIgnore
	private int heartCnt;
	private int commentCnt;

	@ManyToOne
	@JoinColumn
	@JsonBackReference
	private Member member;

	@OneToMany(cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Comment> commentList = new ArrayList<>();

	@OneToMany(mappedBy = "article")
	@JsonIgnore
	private List<Heart> heartList = new ArrayList<>();


	public Article(ArticleRequestDto articleRequestDto, Member member) {
		this.username = member.getUsername();
		this.title = articleRequestDto.getTitle();
		this.content = articleRequestDto.getContent();
		this.member = member;
	}

	public void updateArticle(ArticleRequestDto articleRequestDto) {
		this.title = articleRequestDto.getTitle();
		this.content = articleRequestDto.getContent();
	}

	public void addComment(Comment comment) {
		this.commentList.add(comment);
	}
	public void removeComment(Comment comment) {
		this.commentList.remove(comment);
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
	public void setSelectedComment(Long selectedCommentId) {
		this.selectedCommentId = selectedCommentId;
		this.isDone = true;
	}
	public void setImgUrl(String url) {
		this.imgUrl = url;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}

