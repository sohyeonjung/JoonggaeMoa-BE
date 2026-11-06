package org.silsagusi.batch.infrastructure.dataprovider;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.silsagusi.batch.infrastructure.repository.ArticleRepository;
import org.silsagusi.batch.infrastructure.repository.ComplexRepository;
import org.silsagusi.batch.infrastructure.repository.ScrapeStatusRepository;
import org.silsagusi.core.domain.article.Article;
import org.silsagusi.core.domain.article.Complex;
import org.silsagusi.core.domain.article.ScrapeStatus;

@ExtendWith(MockitoExtension.class)
class ScrapePagePersistenceServiceTest {

    @Mock ArticleRepository articleRepository;
    @Mock ComplexRepository complexRepository;
    @Mock ScrapeStatusRepository statusRepository;
    @InjectMocks ScrapePagePersistenceService service;

    private ScrapeStatus status;

    @BeforeEach
    void setup() {
        status = mock(ScrapeStatus.class);
        lenient().when(statusRepository.findById(7L)).thenReturn(Optional.of(status));
    }

    @Test
    void articlePageSavesOnlyNewCodesAndRecordsCheckpoint() {
        Article existing = mock(Article.class);
        Article fresh = mock(Article.class);
        Article duplicatedWithinPage = mock(Article.class);
        when(existing.getArticleCode()).thenReturn("old");
        when(fresh.getArticleCode()).thenReturn("new");
        when(duplicatedWithinPage.getArticleCode()).thenReturn("new");
        when(articleRepository.findExistingCodes(Set.of("old", "new"))).thenReturn(Set.of("old"));

        service.saveArticlePage(7L, List.of(existing, fresh, duplicatedWithinPage), 4);

        verify(articleRepository).saveAll(List.of(fresh));
        verify(status).checkpoint(4);
    }

    @Test
    void complexPageRecordsCheckpointEvenWhenAllCodesAlreadyExist() {
        Complex existing = mock(Complex.class);
        when(existing.getComplexCode()).thenReturn("old-complex");
        when(complexRepository.findExistingCodes(Set.of("old-complex")))
            .thenReturn(Set.of("old-complex"));

        service.saveComplexPage(7L, List.of(existing), 6);

        verify(complexRepository, never()).saveAll(any());
        verify(status).checkpoint(6);
    }

    @Test
    void pageWithNoArticlesStillAdvancesCheckpoint() {
        service.saveArticlePage(7L, List.of(), 10);

        verifyNoInteractions(articleRepository);
        verify(status).checkpoint(10);
    }

    @Test
    void failedInsertDoesNotUpdateCheckpoint() {
        Article fresh = mock(Article.class);
        when(fresh.getArticleCode()).thenReturn("new");
        when(articleRepository.findExistingCodes(Set.of("new"))).thenReturn(Set.of());
        doThrow(new IllegalStateException("DB insert failed"))
            .when(articleRepository).saveAll(anyList());

        assertThrows(IllegalStateException.class,
            () -> service.saveArticlePage(7L, List.of(fresh), 5));

        verify(status, never()).checkpoint(anyInt());
    }

    @Test
    void completionAndFailureUseExistingCheckpoint() {
        service.complete(7L);
        service.fail(7L);

        verify(status).completed();
        verify(status).markFailed();
        verify(status, never()).checkpoint(anyInt());
    }
}
