package com.stalse.inbox.domain.ticket;

import java.time.Instant;
import java.util.UUID;

public record TicketTriggeredEvent(
        UUID ticketId,
        String customerName,
        String subject,
        TicketStatus status,
        Priority priority,
        Instant occurredAt
) {}
