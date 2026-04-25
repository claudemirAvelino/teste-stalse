package com.stalse.inbox.application.metrics;

import com.stalse.inbox.domain.metrics.CategoryCount;
import com.stalse.inbox.domain.metrics.DailyCount;
import com.stalse.inbox.domain.metrics.Metrics;
import com.stalse.inbox.domain.port.MetricsRepository;
import com.stalse.inbox.domain.ticket.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComputeMetricsUseCaseTest {

    @Mock
    private MetricsRepository repository;

    private final Clock clock = Clock.fixed(Instant.parse("2026-04-25T12:00:00Z"), ZoneOffset.UTC);

    private ComputeMetricsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ComputeMetricsUseCase(repository, clock);
        when(repository.topCategories(anyInt())).thenReturn(List.of());
        when(repository.countByDay(any())).thenReturn(Map.of());
        when(repository.countByStatus()).thenReturn(Map.of());
    }

    @Test
    void completaStatusFaltantesComZero() {
        when(repository.countByStatus()).thenReturn(Map.of(TicketStatus.OPEN, 5L));

        Metrics metrics = useCase.execute(7);

        assertThat(metrics.byStatus())
                .containsEntry(TicketStatus.OPEN, 5L)
                .containsEntry(TicketStatus.IN_PROGRESS, 0L)
                .containsEntry(TicketStatus.CLOSED, 0L);
    }

    @Test
    void preencheDiasFaltantesComZero_naSeriaDiaria() {
        when(repository.countByDay(any())).thenReturn(Map.of(
                LocalDate.parse("2026-04-23"), 2L,
                LocalDate.parse("2026-04-25"), 5L
        ));

        Metrics metrics = useCase.execute(3);

        assertThat(metrics.dailyEvolution()).containsExactly(
                new DailyCount(LocalDate.parse("2026-04-23"), 2L),
                new DailyCount(LocalDate.parse("2026-04-24"), 0L),
                new DailyCount(LocalDate.parse("2026-04-25"), 5L)
        );
    }

    @Test
    void limitaTopCategoriasEm5() {
        useCase.execute(7);

        verify(repository).topCategories(5);
    }

    @Test
    void days_zeroOuNegativo_usaDefaultDe7Dias() {
        Metrics metrics = useCase.execute(0);
        assertThat(metrics.dailyEvolution()).hasSize(7);

        Metrics metricsNeg = useCase.execute(-10);
        assertThat(metricsNeg.dailyEvolution()).hasSize(7);
    }

    @Test
    void days_acimaDoMaximo_aplicaCap90() {
        Metrics metrics = useCase.execute(999);

        assertThat(metrics.dailyEvolution()).hasSize(90);
    }

    @Test
    void mantemListaDeCategoriasNaOrdemRetornadaPeloRepositorio() {
        when(repository.topCategories(5)).thenReturn(List.of(
                new CategoryCount("billing", 5),
                new CategoryCount("bug", 4),
                new CategoryCount("support", 4)
        ));

        Metrics metrics = useCase.execute(7);

        assertThat(metrics.topCategories())
                .extracting(CategoryCount::category)
                .containsExactly("billing", "bug", "support");
    }
}
