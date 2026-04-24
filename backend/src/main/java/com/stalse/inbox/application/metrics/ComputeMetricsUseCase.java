package com.stalse.inbox.application.metrics;

import com.stalse.inbox.domain.metrics.CategoryCount;
import com.stalse.inbox.domain.metrics.DailyCount;
import com.stalse.inbox.domain.metrics.Metrics;
import com.stalse.inbox.domain.port.MetricsRepository;
import com.stalse.inbox.domain.ticket.TicketStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ComputeMetricsUseCase {

    private static final int TOP_CATEGORIES = 5;
    private static final int MAX_DAYS = 90;
    private static final int DEFAULT_DAYS = 7;

    private final MetricsRepository repository;
    private final Clock clock;

    public ComputeMetricsUseCase(MetricsRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Metrics execute(int days) {
        int safeDays = Math.min(MAX_DAYS, Math.max(1, days == 0 ? DEFAULT_DAYS : days));
        LocalDate today = LocalDate.ofInstant(Instant.now(clock), ZoneOffset.UTC);
        LocalDate startDay = today.minusDays(safeDays - 1L);
        Instant since = startDay.atStartOfDay(ZoneOffset.UTC).toInstant();

        Map<TicketStatus, Long> byStatus = completeStatusMap(repository.countByStatus());
        List<CategoryCount> topCategories = repository.topCategories(TOP_CATEGORIES);
        List<DailyCount> dailyEvolution = fillMissingDays(repository.countByDay(since), startDay, today);

        return new Metrics(byStatus, topCategories, dailyEvolution);
    }

    private static Map<TicketStatus, Long> completeStatusMap(Map<TicketStatus, Long> source) {
        Map<TicketStatus, Long> result = new EnumMap<>(TicketStatus.class);
        for (TicketStatus status : TicketStatus.values()) {
            result.put(status, source.getOrDefault(status, 0L));
        }
        return result;
    }

    private static List<DailyCount> fillMissingDays(Map<LocalDate, Long> raw, LocalDate start, LocalDate end) {
        List<DailyCount> series = new ArrayList<>();
        for (LocalDate day = start; !day.isAfter(end); day = day.plusDays(1)) {
            series.add(new DailyCount(day, raw.getOrDefault(day, 0L)));
        }
        return series;
    }
}
