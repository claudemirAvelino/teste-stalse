package com.stalse.inbox.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stalse.inbox.domain.ticket.Priority;
import com.stalse.inbox.domain.ticket.TicketStatus;
import jakarta.validation.constraints.AssertTrue;

public record PatchTicketRequest(TicketStatus status, Priority priority) {

    @JsonIgnore
    @AssertTrue(message = "at least one of 'status' or 'priority' must be provided")
    public boolean isAnyFieldPresent() {
        return status != null || priority != null;
    }
}
