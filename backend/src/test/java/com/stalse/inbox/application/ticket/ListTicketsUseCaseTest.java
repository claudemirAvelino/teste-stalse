package com.stalse.inbox.application.ticket;

import com.stalse.inbox.domain.port.TicketRepository;
import com.stalse.inbox.domain.shared.PagedResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTicketsUseCaseTest {

    @Mock
    private TicketRepository repository;

    @Test
    void aplicaCapEm100_quandoSizeMuitoGrande() {
        when(repository.search(eq("alice"), anyInt(), anyInt()))
                .thenReturn(new PagedResult<>(List.of(), 0, 100, 0));

        new ListTicketsUseCase(repository).execute("alice", 0, 9999);

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(repository).search(eq("alice"), eq(0), sizeCaptor.capture());
        assertThat(sizeCaptor.getValue()).isEqualTo(100);
    }

    @Test
    void aplicaMin1_quandoSizeZeroOuNegativo() {
        when(repository.search(eq(null), anyInt(), anyInt()))
                .thenReturn(new PagedResult<>(List.of(), 0, 1, 0));

        new ListTicketsUseCase(repository).execute(null, 0, -5);

        verify(repository).search(null, 0, 1);
    }

    @Test
    void aplicaMin0_quandoPageNegativa() {
        when(repository.search(eq(null), anyInt(), anyInt()))
                .thenReturn(new PagedResult<>(List.of(), 0, 20, 0));

        new ListTicketsUseCase(repository).execute(null, -3, 20);

        verify(repository).search(null, 0, 20);
    }
}
