package com.hanghea99.minispring.model.dto;

import com.hanghea99.minispring.model.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ArticleResponseDto {

    private String createdDate;
    private Long id;
    private String username;
    private String title;
    private String content;
    private Boolean isDone;

    public ArticleResponseDto(Article article) {
        this.createdDate = article.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.id = article.getId();
        this.username = article.getUsername();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.isDone = article.getIsDone();
    }


}
