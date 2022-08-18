package com.hanghea99.minispring.model.dto;

import com.hanghea99.minispring.model.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private String createdDate;
    private Long id;
    private String username;
    private String comment;


    public CommentResponseDto(Comment comment) {
        this.createdDate = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.id = comment.getId();
        this.comment = comment.getContent();
        this.username = comment.getUsername();
    }
}
