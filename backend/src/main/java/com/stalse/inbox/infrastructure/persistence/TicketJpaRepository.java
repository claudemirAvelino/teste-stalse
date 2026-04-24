package com.stalse.inbox.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TicketJpaRepository extends JpaRepository<TicketJpaEntity, UUID> {

    @Query("""
            SELECT t FROM TicketJpaEntity t
            WHERE LOWER(t.customerName) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(t.subject)      LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(t.category)     LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<TicketJpaEntity> searchByQuery(@Param("q") String q, Pageable pageable);
}
