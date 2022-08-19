package com.hanghea99.minispring.repository;

import com.hanghea99.minispring.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
   List<Comment> findAllById(Long articleId);

    List<Comment> findAllByArticle_Id(Long articleId);
}
