package com.stalse.inbox.infrastructure.persistence;

import com.stalse.inbox.domain.ticket.Ticket;

final class TicketMapper {

    private TicketMapper() {}

    static Ticket toDomain(TicketJpaEntity e) {
        return new Ticket(
                e.getId(),
                e.getCustomerName(),
                e.getChannel(),
                e.getSubject(),
                e.getDescription(),
                e.getCategory(),
                e.getStatus(),
                e.getPriority(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    static void copyToEntity(Ticket source, TicketJpaEntity target) {
        target.setId(source.getId());
        target.setCustomerName(source.getCustomerName());
        target.setChannel(source.getChannel());
        target.setSubject(source.getSubject());
        target.setDescription(source.getDescription());
        target.setCategory(source.getCategory());
        target.setStatus(source.getStatus());
        target.setPriority(source.getPriority());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }
}
