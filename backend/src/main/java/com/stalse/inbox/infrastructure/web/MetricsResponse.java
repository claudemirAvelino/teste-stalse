package com.stalse.inbox.infrastructure.web;

import com.stalse.inbox.domain.metrics.CategoryCount;
import com.stalse.inbox.domain.metrics.DailyCount;
import com.stalse.inbox.domain.metrics.Metrics;
import com.stalse.inbox.domain.ticket.TicketStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record MetricsResponse(
        Map<TicketStatus, Long> byStatus,
        List<CategoryItem> topCategories,
        List<DailyItem> dailyEvolution
) {
    public record CategoryItem(String category, long count) {
        static CategoryItem from(CategoryCount c) {
            return new CategoryItem(c.category(), c.count());
        }
    }

    public record DailyItem(LocalDate date, long count) {
        static DailyItem from(DailyCount d) {
            return new DailyItem(d.date(), d.count());
        }
    }

    public static MetricsResponse from(Metrics m) {
        return new MetricsResponse(
                m.byStatus(),
                m.topCategories().stream().map(CategoryItem::from).toList(),
                m.dailyEvolution().stream().map(DailyItem::from).toList()
        );
    }
}
