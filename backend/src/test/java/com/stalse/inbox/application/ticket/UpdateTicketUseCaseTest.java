package com.stalse.inbox.application.ticket;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.ticket.Channel;
import com.stalse.inbox.domain.ticket.Priority;
import com.stalse.inbox.domain.ticket.Ticket;
import com.stalse.inbox.domain.ticket.TicketNotFoundException;
import com.stalse.inbox.domain.ticket.TicketStatus;
import com.stalse.inbox.domain.ticket.TicketTriggeredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTicketUseCaseTest {

    @Mock
    private TicketRepository repository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private final Clock clock = Clock.fixed(Instant.parse("2026-04-25T12:00:00Z"), ZoneOffset.UTC);

    private UpdateTicketUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateTicketUseCase(repository, clock, eventPublisher);
    }

    @Test
    void mudaStatusParaCLOSED_publicaEventoComDadosDoTicket() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.MEDIUM);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(ticket.getId(), TicketStatus.CLOSED, null);

        ArgumentCaptor<TicketTriggeredEvent> captor = ArgumentCaptor.forClass(TicketTriggeredEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        TicketTriggeredEvent event = captor.getValue();
        assertThat(event.ticketId()).isEqualTo(ticket.getId());
        assertThat(event.status()).isEqualTo(TicketStatus.CLOSED);
        assertThat(event.priority()).isEqualTo(Priority.MEDIUM);
        assertThat(event.occurredAt()).isEqualTo(Instant.parse("2026-04-25T12:00:00Z"));
    }

    @Test
    void mudaPriorityParaHIGH_publicaEvento() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.LOW);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(ticket.getId(), null, Priority.HIGH);

        verify(eventPublisher).publishEvent(any(TicketTriggeredEvent.class));
    }

    @Test
    void mudaStatusParaIN_PROGRESS_naoPublicaEvento() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.LOW);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(ticket.getId(), TicketStatus.IN_PROGRESS, null);

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void mudaPriorityParaLOW_naoPublicaEvento() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.HIGH);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(ticket.getId(), null, Priority.LOW);

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void valoresIguaisAoAtual_naoChamaSaveNemPublicaEvento() {
        Ticket ticket = newTicket(TicketStatus.CLOSED, Priority.HIGH);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

        useCase.execute(ticket.getId(), TicketStatus.CLOSED, Priority.HIGH);

        verify(repository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void mudaStatusEPriority_publicaEventoUmaUnicaVez() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.LOW);
        when(repository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(ticket.getId(), TicketStatus.CLOSED, Priority.HIGH);

        verify(eventPublisher, times(1)).publishEvent(any(TicketTriggeredEvent.class));
    }

    @Test
    void ticketInexistente_lancaTicketNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, TicketStatus.CLOSED, null))
                .isInstanceOf(TicketNotFoundException.class);

        verify(repository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    private static Ticket newTicket(TicketStatus status, Priority priority) {
        Instant past = Instant.parse("2026-04-20T10:00:00Z");
        return new Ticket(
                UUID.randomUUID(), "Alice Souza", Channel.EMAIL, "Cobranca",
                null, "billing", status, priority, past, past);
    }
}
