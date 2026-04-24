package com.stalse.inbox.infrastructure.persistence;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.shared.PagedResult;
import com.stalse.inbox.domain.ticket.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class TicketRepositoryAdapter implements TicketRepository {

    private final TicketJpaRepository jpa;

    public TicketRepositoryAdapter(TicketJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @Transactional
    public Ticket save(Ticket ticket) {
        TicketJpaEntity entity = jpa.findById(ticket.getId()).orElseGet(TicketJpaEntity::new);
        TicketMapper.copyToEntity(ticket, entity);
        return TicketMapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Ticket> findById(UUID id) {
        return jpa.findById(id).map(TicketMapper::toDomain);
    }

    @Override
    public PagedResult<Ticket> search(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        String q = (query == null || query.isBlank()) ? null : query.trim();
        Page<TicketJpaEntity> result = (q == null)
                ? jpa.findAll(pageable)
                : jpa.searchByQuery(q, pageable);
        return new PagedResult<>(
                result.getContent().stream().map(TicketMapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }
}
