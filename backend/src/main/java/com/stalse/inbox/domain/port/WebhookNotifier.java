package com.stalse.inbox.domain.port;

import com.stalse.inbox.domain.ticket.TicketTriggeredEvent;

public interface WebhookNotifier {

    void notify(TicketTriggeredEvent event);
}
