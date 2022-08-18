package com.hanghea99.minispring.model.dto;

import com.hanghea99.minispring.model.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private String comment;

    public CommentRequestDto(Comment comment) {
        this.comment = comment.getContent();
    }
}
