package com.stalse.inbox.domain.ticket;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketTest {

    @Test
    void changeStatus_retornaTrueEAtualizaTimestamp_quandoStatusMuda() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.LOW);
        Instant now = Instant.parse("2026-04-23T12:00:00Z");

        boolean changed = ticket.changeStatus(TicketStatus.CLOSED, now);

        assertTrue(changed);
        assertEquals(TicketStatus.CLOSED, ticket.getStatus());
        assertEquals(now, ticket.getUpdatedAt());
    }

    @Test
    void changeStatus_retornaFalseENaoAtualizaTimestamp_quandoStatusNaoMuda() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.LOW);
        Instant before = ticket.getUpdatedAt();

        boolean changed = ticket.changeStatus(TicketStatus.OPEN, Instant.now());

        assertFalse(changed);
        assertEquals(before, ticket.getUpdatedAt());
    }

    @Test
    void changePriority_retornaTrueEAtualizaTimestamp_quandoPrioridadeMuda() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.LOW);
        Instant now = Instant.parse("2026-04-23T12:00:00Z");

        boolean changed = ticket.changePriority(Priority.HIGH, now);

        assertTrue(changed);
        assertEquals(Priority.HIGH, ticket.getPriority());
        assertEquals(now, ticket.getUpdatedAt());
    }

    @Test
    void changePriority_retornaFalse_quandoPrioridadeNaoMuda() {
        Ticket ticket = newTicket(TicketStatus.OPEN, Priority.HIGH);

        boolean changed = ticket.changePriority(Priority.HIGH, Instant.now());

        assertFalse(changed);
    }

    @Test
    void construtor_rejeitaCamposObrigatoriosEmBranco() {
        Instant now = Instant.now();
        UUID id = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> new Ticket(
                id, "  ", Channel.EMAIL, "Subject", null,
                "billing", TicketStatus.OPEN, Priority.LOW, now, now));

        assertThrows(IllegalArgumentException.class, () -> new Ticket(
                id, "Alice", Channel.EMAIL, "", null,
                "billing", TicketStatus.OPEN, Priority.LOW, now, now));
    }

    @Test
    void ticketStatus_apenasCLOSED_disparaWebhook() {
        assertTrue(TicketStatus.CLOSED.triggersWebhook());
        assertFalse(TicketStatus.OPEN.triggersWebhook());
        assertFalse(TicketStatus.IN_PROGRESS.triggersWebhook());
    }

    @Test
    void priority_apenasHIGH_disparaWebhook() {
        assertTrue(Priority.HIGH.triggersWebhook());
        assertFalse(Priority.MEDIUM.triggersWebhook());
        assertFalse(Priority.LOW.triggersWebhook());
    }

    private static Ticket newTicket(TicketStatus status, Priority priority) {
        Instant now = Instant.parse("2026-04-20T10:00:00Z");
        return new Ticket(
                UUID.randomUUID(),
                "Alice Souza",
                Channel.EMAIL,
                "Cobranca duplicada",
                null,
                "billing",
                status,
                priority,
                now,
                now
        );
    }
}
