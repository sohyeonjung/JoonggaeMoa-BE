package org.silsagusi.batch.infrastructure.repository;

import java.util.Optional;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.silsagusi.core.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Optional<Object> findFirstByArticleCode(String articleCode);

	@Query("select a.articleCode from Article a where a.articleCode in :codes")
	Set<String> findExistingCodes(@Param("codes") Collection<String> codes);
}
