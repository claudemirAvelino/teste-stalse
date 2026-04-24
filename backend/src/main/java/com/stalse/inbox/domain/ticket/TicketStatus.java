package com.stalse.inbox.domain.ticket;

public enum TicketStatus {
    OPEN,
    IN_PROGRESS,
    CLOSED;

    public boolean triggersWebhook() {
        return this == CLOSED;
    }
}
