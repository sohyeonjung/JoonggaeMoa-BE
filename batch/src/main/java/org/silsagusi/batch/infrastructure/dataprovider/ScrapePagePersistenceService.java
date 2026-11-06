package org.silsagusi.batch.infrastructure.dataprovider;

import java.util.List;

import org.silsagusi.batch.infrastructure.repository.ArticleRepository;
import org.silsagusi.batch.infrastructure.repository.ComplexRepository;
import org.silsagusi.batch.infrastructure.repository.ScrapeStatusRepository;
import org.silsagusi.core.domain.article.Article;
import org.silsagusi.core.domain.article.Complex;
import org.silsagusi.core.domain.article.ScrapeStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * Commits one externally fetched page and its progress checkpoint in a short
 * business-DB transaction. Do not call an external API or sleep in this service.
 */
@Service
@RequiredArgsConstructor
public class ScrapePagePersistenceService {

    private final ArticleRepository articleRepository;
    private final ComplexRepository complexRepository;
    private final ScrapeStatusRepository statusRepository;

    @Transactional(transactionManager = "dataTransactionManager")
    public void saveArticlePage(Long statusId, List<Article> articles, int nextCursor) {
        for (Article article : articles) {
            if (article.getArticleCode() != null
                && articleRepository.findFirstByArticleCode(article.getArticleCode()).isEmpty()) {
                articleRepository.save(article);
            }
        }
        checkpoint(statusId, nextCursor);
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public void saveComplexPage(Long statusId, List<Complex> complexes, int nextCursor) {
        for (Complex complex : complexes) {
            if (complex.getComplexCode() != null
                && complexRepository.findFirstByComplexCode(complex.getComplexCode()).isEmpty()) {
                complexRepository.save(complex);
            }
        }
        checkpoint(statusId, nextCursor);
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public void complete(Long statusId) {
        getStatus(statusId).completed();
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public void fail(Long statusId) {
        // Preserve the last *committed* cursor; the failing page can be retried.
        getStatus(statusId).markFailed();
    }

    private void checkpoint(Long statusId, int nextCursor) {
        getStatus(statusId).checkpoint(nextCursor);
    }

    private ScrapeStatus getStatus(Long statusId) {
        return statusRepository.findById(statusId)
            .orElseThrow(() -> new IllegalStateException("Missing scrape status: " + statusId));
    }
}
