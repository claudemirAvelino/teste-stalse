package com.stalse.inbox.application.ticket;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.ticket.Ticket;
import com.stalse.inbox.domain.ticket.TicketNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetTicketUseCase {

    private final TicketRepository repository;

    public GetTicketUseCase(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
}
