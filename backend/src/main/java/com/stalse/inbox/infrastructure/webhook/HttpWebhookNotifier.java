package com.stalse.inbox.infrastructure.webhook;

import com.stalse.inbox.domain.port.WebhookNotifier;
import com.stalse.inbox.domain.ticket.TicketTriggeredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HttpWebhookNotifier implements WebhookNotifier {

    private static final Logger log = LoggerFactory.getLogger(HttpWebhookNotifier.class);

    private final RestClient restClient;
    private final String webhookUrl;

    public HttpWebhookNotifier(
            RestClient.Builder builder,
            @Value("${inbox.webhook.url}") String webhookUrl
    ) {
        this.restClient = builder.build();
        this.webhookUrl = webhookUrl;
    }

    @Override
    @Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2)
    )
    public void notify(TicketTriggeredEvent event) {
        log.info("Dispatching webhook for ticket {} (status={}, priority={})",
                event.ticketId(), event.status(), event.priority());
        restClient.post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(event)
                .retrieve()
                .toBodilessEntity();
        log.info("Webhook delivered for ticket {}", event.ticketId());
    }

    @Recover
    public void recover(RuntimeException ex, TicketTriggeredEvent event) {
        log.warn("Webhook delivery failed after retries for ticket {}: {}",
                event.ticketId(), ex.getMessage());
    }
}
