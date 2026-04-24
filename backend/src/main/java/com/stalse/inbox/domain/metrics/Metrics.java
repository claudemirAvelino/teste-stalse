package com.stalse.inbox.domain.metrics;

import com.stalse.inbox.domain.ticket.TicketStatus;

import java.util.List;
import java.util.Map;

public record Metrics(
        Map<TicketStatus, Long> byStatus,
        List<CategoryCount> topCategories,
        List<DailyCount> dailyEvolution
) {}
