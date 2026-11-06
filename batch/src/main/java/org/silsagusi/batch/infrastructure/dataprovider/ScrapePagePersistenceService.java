package org.silsagusi.batch.infrastructure.dataprovider;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<String> codes = articles.stream().map(Article::getArticleCode)
            .filter(code -> code != null).collect(Collectors.toSet());
        Set<String> existing = codes.isEmpty() ? Set.of() : articleRepository.findExistingCodes(codes);
        Set<String> seen = new java.util.HashSet<>(existing);
        List<Article> newArticles = articles.stream()
            .filter(article -> article.getArticleCode() != null && seen.add(article.getArticleCode()))
            .toList();
        if (!newArticles.isEmpty()) {
            articleRepository.saveAll(newArticles);
        }
        checkpoint(statusId, nextCursor);
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public void saveComplexPage(Long statusId, List<Complex> complexes, int nextCursor) {
        Set<String> codes = complexes.stream().map(Complex::getComplexCode)
            .filter(code -> code != null).collect(Collectors.toSet());
        Set<String> existing = codes.isEmpty() ? Set.of() : complexRepository.findExistingCodes(codes);
        Set<String> seen = new java.util.HashSet<>(existing);
        List<Complex> newComplexes = complexes.stream()
            .filter(complex -> complex.getComplexCode() != null && seen.add(complex.getComplexCode()))
            .toList();
        if (!newComplexes.isEmpty()) {
            complexRepository.saveAll(newComplexes);
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
