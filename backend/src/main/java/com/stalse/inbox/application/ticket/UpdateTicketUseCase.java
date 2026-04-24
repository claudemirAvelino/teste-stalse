package com.stalse.inbox.application.ticket;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.ticket.Priority;
import com.stalse.inbox.domain.ticket.Ticket;
import com.stalse.inbox.domain.ticket.TicketNotFoundException;
import com.stalse.inbox.domain.ticket.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateTicketUseCase {

    private final TicketRepository repository;
    private final Clock clock;

    public UpdateTicketUseCase(TicketRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Ticket execute(UUID id, TicketStatus newStatus, Priority newPriority) {
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        Instant now = Instant.now(clock);
        boolean changed = false;
        if (newStatus != null) {
            changed |= ticket.changeStatus(newStatus, now);
        }
        if (newPriority != null) {
            changed |= ticket.changePriority(newPriority, now);
        }

        return changed ? repository.save(ticket) : ticket;
    }
}
