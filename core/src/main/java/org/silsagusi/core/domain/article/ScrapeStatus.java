package org.silsagusi.core.domain.article;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.silsagusi.core.domain.article.enums.Platform;
import org.silsagusi.core.domain.article.enums.ScrapeStatusType;
import org.silsagusi.core.domain.article.enums.ScrapeTargetType;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
	name = "scrape_statuses",
	uniqueConstraints = @UniqueConstraint(
		name = "scrape_status_unique_constraint",
		columnNames = {"region_id", "platform", "target_type"}))
@Getter
public class ScrapeStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scrape_status_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;

	@Column(name = "last_scraped_page")
	private Integer lastScrapedPage;

	@Column(name = "last_scraped_at")
	private LocalDateTime lastScrapedAt;

	@Enumerated(EnumType.STRING)
	private ScrapeStatusType status;

	@Enumerated(EnumType.STRING)
	private Platform platform;

	@Enumerated(EnumType.STRING)
	@Column(name = "target_type")
	private ScrapeTargetType targetType;

	public ScrapeStatus initialize() {
		this.status = ScrapeStatusType.PENDING;
		this.lastScrapedPage = 0;
		return this;
	}

	/** Naver stores the last successful page; Zigbang stores the next offset. */
	public void checkpoint(int cursor) {
		this.lastScrapedPage = cursor;
		this.lastScrapedAt = LocalDateTime.now();
	}

	/** Keep checkpoint intact so a failed page is retried, not skipped. */
	public void markFailed() {
		this.status = ScrapeStatusType.FAILED;
		this.lastScrapedAt = LocalDateTime.now();
	}

	public ScrapeStatus completed() {
		this.status = ScrapeStatusType.COMPLETED;
		this.lastScrapedAt = LocalDateTime.now();
		return this;
	}

	public ScrapeStatus failed(int page) {
		this.status = ScrapeStatusType.FAILED;
		this.lastScrapedPage = page;
		this.lastScrapedAt = LocalDateTime.now();
		return this;
	}
}
