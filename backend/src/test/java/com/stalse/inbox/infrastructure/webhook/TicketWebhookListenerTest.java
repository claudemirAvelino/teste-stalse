package com.stalse.inbox.infrastructure.webhook;

import com.stalse.inbox.domain.port.WebhookNotifier;
import com.stalse.inbox.domain.ticket.Priority;
import com.stalse.inbox.domain.ticket.TicketStatus;
import com.stalse.inbox.domain.ticket.TicketTriggeredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TicketWebhookListenerTest {

    @Mock
    private WebhookNotifier notifier;

    @Test
    void onTicketTriggered_delegaParaWebhookNotifier() {
        TicketWebhookListener listener = new TicketWebhookListener(notifier);
        TicketTriggeredEvent event = new TicketTriggeredEvent(
                UUID.randomUUID(),
                "Alice",
                "Cobranca",
                TicketStatus.CLOSED,
                Priority.MEDIUM,
                Instant.parse("2026-04-25T12:00:00Z")
        );

        listener.onTicketTriggered(event);

        verify(notifier).notify(event);
    }
}
