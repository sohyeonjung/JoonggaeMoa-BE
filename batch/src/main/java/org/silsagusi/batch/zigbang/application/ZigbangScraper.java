package org.silsagusi.batch.zigbang.application;

import java.util.List;
import java.util.Random;

import org.silsagusi.batch.application.ArticleMapper;
import org.silsagusi.batch.application.ArticleValidator;
import org.silsagusi.batch.application.ComplexMapper;
import org.silsagusi.batch.application.ComplexValidator;
import org.silsagusi.batch.infrastructure.dataprovider.ArticleDataProvider;
import org.silsagusi.batch.infrastructure.dataprovider.ComplexDataProvider;
import org.silsagusi.batch.infrastructure.dataprovider.ScrapePagePersistenceService;
import org.silsagusi.batch.zigbang.infrastructure.ZigBangApiClient;
import org.silsagusi.batch.zigbang.infrastructure.dto.ZigBangDanjiResponse;
import org.silsagusi.batch.zigbang.infrastructure.dto.ZigBangItemCatalogResponse;
import org.silsagusi.core.domain.article.Article;
import org.silsagusi.core.domain.article.Complex;
import org.silsagusi.core.domain.article.Region;
import org.silsagusi.core.domain.article.ScrapeStatus;
import org.silsagusi.core.domain.article.enums.Platform;
import org.silsagusi.core.domain.article.enums.ScrapeTargetType;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ZigbangScraper {

	private static final String BASE_32 = "0123456789bcdefghjkmnpqrstuvwxyz";
	private static final int SCRAPE_ARTICLE_LIMIT = 10;
	private final Random random = new Random();

	private final ZigBangApiClient zigBangApiClient;
	private final ArticleDataProvider articleDataProvider;
	private final ComplexDataProvider complexDataProvider;
	private final ScrapePagePersistenceService pagePersistenceService;
	private final ComplexMapper complexMapper;
	private final ArticleMapper articleMapper;
	private final ArticleValidator articleValidator;
	private final ComplexValidator complexValidator;

	public ScrapeStatus scrapComplex(ScrapeStatus status) throws InterruptedException {
		Region region = status.getRegion();
		String geohashPrefix = region.getGeohash().substring(0, 4);

		for (int page = status.getLastScrapedPage(); page < 32; page++) {
			char fifth = BASE_32.charAt(page);
			try {
				ZigBangDanjiResponse zigBangDanjiResponse = zigBangApiClient.fetchDanji(geohashPrefix + fifth);

				List<Complex> complexes = zigBangDanjiResponse.getFiltered().stream()
					.map(response -> complexMapper.toEntity(response, region))
					.toList();

				pagePersistenceService.saveComplexPage(status.getId(), complexes, page + 1);
				status.checkpoint(page + 1);
			} catch (Exception e) {
				log.error(
					"스크래핑 실패: {} - {}. region_id: {} - {}페이지. 좌표값: {},{}. 에러 내용: {}",
					Platform.ZIGBANG, ScrapeTargetType.COMPLEX, region.getId(), page,
					region.getCenterLat(), region.getCenterLon(), e.getMessage(), e);
				Thread.sleep(600_000);
				status.markFailed();
				return status;
			}
			Thread.sleep((3000 + random.nextInt(4000)));
		}
		return status.completed();
	}

	public ScrapeStatus scrapArticle(ScrapeStatus status) throws InterruptedException {
		int offset = status.getLastScrapedPage();
		Region region = status.getRegion();
		String localCode = region.getCortarNo().substring(0, 8);
		boolean hasMore = true;

		while (hasMore) {
			try {
				ZigBangItemCatalogResponse zigBangItemCatalogResponse = zigBangApiClient.fetchItemCatalog(localCode,
					offset, SCRAPE_ARTICLE_LIMIT);

				List<Article> articles = zigBangItemCatalogResponse.getList().stream()
					.map(response -> {
						Complex complex = complexDataProvider.getComplexByComplexCode(
							String.valueOf(response.getAreaDanjiId()));
						return articleMapper.toEntity(response, region, complex);
					})
					.toList();

				pagePersistenceService.saveArticlePage(status.getId(), articles, offset + SCRAPE_ARTICLE_LIMIT);
				status.checkpoint(offset + SCRAPE_ARTICLE_LIMIT);

				hasMore = zigBangItemCatalogResponse.getCount() != null
					&& offset + SCRAPE_ARTICLE_LIMIT < zigBangItemCatalogResponse.getCount()
					&& !zigBangItemCatalogResponse.getList().isEmpty();
			} catch (Exception e) {
				log.error(
					"스크래핑 실패: {} - {}. region_id: {} - {}페이지. 좌표값: {},{}. 에러 내용: {}",
					Platform.ZIGBANG, ScrapeTargetType.ARTICLE, region.getId(), offset,
					region.getCenterLat(), region.getCenterLon(), e.getMessage(), e);
				Thread.sleep(600_000);
				status.markFailed();
				return status;
			}
			offset += SCRAPE_ARTICLE_LIMIT;
			Thread.sleep((3000 + random.nextInt(4000)));
		}
		return status.completed();
	}
}
