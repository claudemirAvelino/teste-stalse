package com.stalse.inbox.infrastructure.web;

import com.stalse.inbox.domain.ticket.Channel;
import com.stalse.inbox.domain.ticket.Priority;
import com.stalse.inbox.domain.ticket.Ticket;
import com.stalse.inbox.domain.ticket.TicketStatus;

import java.time.Instant;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String customerName,
        Channel channel,
        String subject,
        String description,
        String category,
        TicketStatus status,
        Priority priority,
        Instant createdAt,
        Instant updatedAt
) {
    public static TicketResponse from(Ticket t) {
        return new TicketResponse(
                t.getId(),
                t.getCustomerName(),
                t.getChannel(),
                t.getSubject(),
                t.getDescription(),
                t.getCategory(),
                t.getStatus(),
                t.getPriority(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
