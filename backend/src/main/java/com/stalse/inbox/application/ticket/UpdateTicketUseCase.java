package com.stalse.inbox.application.ticket;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.ticket.Priority;
import com.stalse.inbox.domain.ticket.Ticket;
import com.stalse.inbox.domain.ticket.TicketNotFoundException;
import com.stalse.inbox.domain.ticket.TicketStatus;
import com.stalse.inbox.domain.ticket.TicketTriggeredEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateTicketUseCase {

    private final TicketRepository repository;
    private final Clock clock;
    private final ApplicationEventPublisher eventPublisher;

    public UpdateTicketUseCase(
            TicketRepository repository,
            Clock clock,
            ApplicationEventPublisher eventPublisher
    ) {
        this.repository = repository;
        this.clock = clock;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Ticket execute(UUID id, TicketStatus newStatus, Priority newPriority) {
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        Instant now = Instant.now(clock);
        boolean statusChanged = newStatus != null && ticket.changeStatus(newStatus, now);
        boolean priorityChanged = newPriority != null && ticket.changePriority(newPriority, now);

        if (!statusChanged && !priorityChanged) {
            return ticket;
        }

        Ticket saved = repository.save(ticket);

        boolean triggersWebhook =
                (statusChanged && newStatus.triggersWebhook()) ||
                (priorityChanged && newPriority.triggersWebhook());

        if (triggersWebhook) {
            eventPublisher.publishEvent(new TicketTriggeredEvent(
                    saved.getId(),
                    saved.getCustomerName(),
                    saved.getSubject(),
                    saved.getStatus(),
                    saved.getPriority(),
                    now
            ));
        }

        return saved;
    }
}
