package com.stalse.inbox.domain.port;

import com.stalse.inbox.domain.metrics.CategoryCount;
import com.stalse.inbox.domain.ticket.TicketStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MetricsRepository {

    Map<TicketStatus, Long> countByStatus();

    List<CategoryCount> topCategories(int limit);

    Map<LocalDate, Long> countByDay(Instant since);
}
