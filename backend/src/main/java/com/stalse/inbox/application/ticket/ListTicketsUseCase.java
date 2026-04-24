package com.stalse.inbox.application.ticket;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.shared.PagedResult;
import com.stalse.inbox.domain.ticket.Ticket;
import org.springframework.stereotype.Service;

@Service
public class ListTicketsUseCase {

    private static final int MAX_PAGE_SIZE = 100;

    private final TicketRepository repository;

    public ListTicketsUseCase(TicketRepository repository) {
        this.repository = repository;
    }

    public PagedResult<Ticket> execute(String query, int page, int size) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size));
        return repository.search(query, safePage, safeSize);
    }
}
