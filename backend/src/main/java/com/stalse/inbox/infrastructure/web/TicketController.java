package com.stalse.inbox.infrastructure.web;

import com.stalse.inbox.application.ticket.GetTicketUseCase;
import com.stalse.inbox.application.ticket.ListTicketsUseCase;
import com.stalse.inbox.application.ticket.UpdateTicketUseCase;
import com.stalse.inbox.domain.shared.PagedResult;
import com.stalse.inbox.domain.ticket.Ticket;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final ListTicketsUseCase listTickets;
    private final GetTicketUseCase getTicket;
    private final UpdateTicketUseCase updateTicket;

    public TicketController(
            ListTicketsUseCase listTickets,
            GetTicketUseCase getTicket,
            UpdateTicketUseCase updateTicket
    ) {
        this.listTickets = listTickets;
        this.getTicket = getTicket;
        this.updateTicket = updateTicket;
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

    @GetMapping("/{id}")
    public TicketResponse getById(@PathVariable UUID id) {
        return TicketResponse.from(getTicket.execute(id));
    }

    @PatchMapping("/{id}")
    public TicketResponse patch(
            @PathVariable UUID id,
            @Valid @RequestBody PatchTicketRequest request
    ) {
        Ticket updated = updateTicket.execute(id, request.status(), request.priority());
        return TicketResponse.from(updated);
    }
}
