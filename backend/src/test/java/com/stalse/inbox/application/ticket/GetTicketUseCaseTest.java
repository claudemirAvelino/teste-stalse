package com.stalse.inbox.application.ticket;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.ticket.Channel;
import com.stalse.inbox.domain.ticket.Priority;
import com.stalse.inbox.domain.ticket.Ticket;
import com.stalse.inbox.domain.ticket.TicketNotFoundException;
import com.stalse.inbox.domain.ticket.TicketStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTicketUseCaseTest {

    @Mock
    private TicketRepository repository;

    @Test
    void retornaTicket_quandoExiste() {
        Ticket ticket = sample();
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        Ticket result = new GetTicketUseCase(repository).execute(ticket.getId());

        assertThat(result).isSameAs(ticket);
    }

    @Test
    void lancaException_quandoNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> new GetTicketUseCase(repository).execute(id))
                .isInstanceOf(TicketNotFoundException.class);
    }

    private static Ticket sample() {
        Instant now = Instant.parse("2026-04-25T10:00:00Z");
        return new Ticket(UUID.randomUUID(), "Alice", Channel.EMAIL, "Subj",
                null, "billing", TicketStatus.OPEN, Priority.LOW, now, now);
    }
}
