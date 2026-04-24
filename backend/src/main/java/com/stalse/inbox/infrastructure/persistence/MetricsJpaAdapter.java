package com.stalse.inbox.infrastructure.persistence;

import com.stalse.inbox.domain.metrics.CategoryCount;
import com.stalse.inbox.domain.port.MetricsRepository;
import com.stalse.inbox.domain.ticket.TicketStatus;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional(readOnly = true)
public class MetricsJpaAdapter implements MetricsRepository {

    private final EntityManager em;

    public MetricsJpaAdapter(EntityManager em) {
        this.em = em;
    }

    @Override
    public Map<TicketStatus, Long> countByStatus() {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createQuery(
                "SELECT t.status, COUNT(t) FROM TicketJpaEntity t GROUP BY t.status"
        ).getResultList();

        Map<TicketStatus, Long> result = new EnumMap<>(TicketStatus.class);
        for (Object[] row : rows) {
            result.put((TicketStatus) row[0], (Long) row[1]);
        }
        return result;
    }

    @Override
    public List<CategoryCount> topCategories(int limit) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createQuery(
                        "SELECT t.category, COUNT(t) FROM TicketJpaEntity t " +
                        "GROUP BY t.category ORDER BY COUNT(t) DESC, t.category ASC")
                .setMaxResults(limit)
                .getResultList();

        return rows.stream()
                .map(row -> new CategoryCount((String) row[0], (Long) row[1]))
                .toList();
    }

    @Override
    public Map<LocalDate, Long> countByDay(Instant since) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery("""
                        SELECT DATE(created_at AT TIME ZONE 'UTC') AS day, COUNT(*) AS total
                        FROM tickets
                        WHERE created_at >= :since
                        GROUP BY day
                        ORDER BY day
                        """)
                .setParameter("since", since)
                .getResultList();

        Map<LocalDate, Long> result = new LinkedHashMap<>();
        for (Object[] row : rows) {
            LocalDate day = ((java.sql.Date) row[0]).toLocalDate();
            long count = ((Number) row[1]).longValue();
            result.put(day, count);
        }
        return result;
    }
}
