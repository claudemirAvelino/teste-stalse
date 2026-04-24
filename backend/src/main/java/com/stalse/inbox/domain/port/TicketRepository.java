package com.stalse.inbox.domain.port;

import com.stalse.inbox.domain.shared.PagedResult;
import com.stalse.inbox.domain.ticket.Ticket;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    Optional<Ticket> findById(UUID id);

    PagedResult<Ticket> search(String query, int page, int size);
}
