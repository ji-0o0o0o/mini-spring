package com.hanghea99.minispring.repository;

import com.hanghea99.minispring.model.Article;
import com.hanghea99.minispring.model.Comment;
import com.hanghea99.minispring.model.Heart;
import com.hanghea99.minispring.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Heart findByMemberAndArticle(Member member, Article article);

    Heart findByMemberAndComment(Member member, Comment comment);
}
