package com.stalse.inbox.infrastructure.web;

import com.stalse.inbox.application.ticket.ListTicketsUseCase;
import com.stalse.inbox.domain.shared.PagedResult;
import com.stalse.inbox.domain.ticket.Ticket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final ListTicketsUseCase listTickets;

    public TicketController(ListTicketsUseCase listTickets) {
        this.listTickets = listTickets;
    }

    @GetMapping
    public PageResponse<TicketResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PagedResult<Ticket> result = listTickets.execute(query, page, size);
        return PageResponse.map(result, TicketResponse::from);
    }
}
