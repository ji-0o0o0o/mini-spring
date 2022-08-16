package com.hanghea99.minispring.repository;

import com.hanghea99.minispring.model.Article;
import com.hanghea99.minispring.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	List<Article> findAllByLanguage(Language language);
}
