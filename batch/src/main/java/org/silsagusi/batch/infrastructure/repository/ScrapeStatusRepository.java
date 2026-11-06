package org.silsagusi.batch.infrastructure.repository;

import org.silsagusi.core.domain.article.ScrapeStatus;
import org.silsagusi.core.domain.article.enums.Platform;
import org.silsagusi.core.domain.article.enums.ScrapeStatusType;
import org.silsagusi.core.domain.article.enums.ScrapeTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapeStatusRepository extends JpaRepository<ScrapeStatus, Long> {

	// List<ScrapeStatus> findTop10BySourceAndCompletedFalseOrderByIdAsc(String source);
	//
	// List<ScrapeStatus> findTop1ByCompletedFalseOrderByIdAsc();

	@Query("select s from ScrapeStatus s join fetch s.region where s.platform = :platform and s.targetType = :targetType and s.status = :status order by s.id asc limit 1")
	ScrapeStatus findNextWithRegion(@Param("platform") Platform platform,
		@Param("targetType") ScrapeTargetType targetType, @Param("status") ScrapeStatusType status);

	// @Modifying(clearAutomatically = true)
	// @Transactional
	// @Query("""
	// 	UPDATE ScrapeStatus s
	// 	SET s.completed = false,
	// 	    s.failed = false,
	// 	    s.errorMessage = null,
	// 	    s.lastScrapedAt = null,
	// 	    s.lastScrapedPage = 1
	// 	WHERE s.lastScrapedAt < :cutoff
	// 	""")
	// void resetAllScrapeStatus(@Param("cutoff") LocalDateTime cutoff);
}
