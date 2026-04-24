package com.stalse.inbox.infrastructure.webhook;

import com.stalse.inbox.domain.port.WebhookNotifier;
import com.stalse.inbox.domain.ticket.TicketTriggeredEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TicketWebhookListener {

    private final WebhookNotifier notifier;

    public TicketWebhookListener(WebhookNotifier notifier) {
        this.notifier = notifier;
    }

    @Async("webhookExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTicketTriggered(TicketTriggeredEvent event) {
        notifier.notify(event);
    }
}
