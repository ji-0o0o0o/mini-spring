package com.hanghea99.minispring.model.dto;

import lombok.Getter;

@Getter
public class ArticleRequestDto {

    private String title;
    private String content;
    private String language = "";
}
