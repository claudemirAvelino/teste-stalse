package com.stalse.inbox.domain.ticket;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Ticket {

    private final UUID id;
    private final String customerName;
    private final Channel channel;
    private final String subject;
    private final String description;
    private final String category;
    private TicketStatus status;
    private Priority priority;
    private final Instant createdAt;
    private Instant updatedAt;

    public Ticket(
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
        this.id = Objects.requireNonNull(id, "id");
        this.customerName = requireNonBlank(customerName, "customerName");
        this.channel = Objects.requireNonNull(channel, "channel");
        this.subject = requireNonBlank(subject, "subject");
        this.description = description;
        this.category = requireNonBlank(category, "category");
        this.status = Objects.requireNonNull(status, "status");
        this.priority = Objects.requireNonNull(priority, "priority");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public boolean changeStatus(TicketStatus newStatus, Instant now) {
        Objects.requireNonNull(newStatus, "newStatus");
        Objects.requireNonNull(now, "now");
        if (this.status == newStatus) {
            return false;
        }
        this.status = newStatus;
        this.updatedAt = now;
        return true;
    }

    public boolean changePriority(Priority newPriority, Instant now) {
        Objects.requireNonNull(newPriority, "newPriority");
        Objects.requireNonNull(now, "now");
        if (this.priority == newPriority) {
            return false;
        }
        this.priority = newPriority;
        this.updatedAt = now;
        return true;
    }

    public UUID getId() { return id; }
    public String getCustomerName() { return customerName; }
    public Channel getChannel() { return channel; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public TicketStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
