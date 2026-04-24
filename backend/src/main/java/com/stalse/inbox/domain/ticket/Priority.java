package com.stalse.inbox.domain.ticket;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH;

    public boolean triggersWebhook() {
        return this == HIGH;
    }
}
