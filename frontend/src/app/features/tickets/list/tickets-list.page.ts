import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { catchError, debounceTime, distinctUntilChanged, of, switchMap, tap } from 'rxjs';

import { Ticket, TicketChannel } from '../../../core/models/ticket.model';
import { CardComponent } from '../../../shared/ui/card.component';
import { EmptyStateComponent } from '../../../shared/ui/empty-state.component';
import { PriorityBadgeComponent } from '../../../shared/ui/priority-badge.component';
import { SkeletonComponent } from '../../../shared/ui/skeleton.component';
import { StatusBadgeComponent } from '../../../shared/ui/status-badge.component';
import { TicketsService } from '../data/tickets.service';

const CHANNEL_LABELS: Record<TicketChannel, string> = {
  EMAIL: 'E-mail',
  WHATSAPP: 'WhatsApp',
  CHAT: 'Chat',
  PHONE: 'Telefone'
};

@Component({
  selector: 'app-tickets-list-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    DatePipe,
    CardComponent,
    EmptyStateComponent,
    PriorityBadgeComponent,
    SkeletonComponent,
    StatusBadgeComponent
  ],
  templateUrl: './tickets-list.page.html'
})
export class TicketsListPage {
  private readonly tickets = inject(TicketsService);
  private readonly router = inject(Router);

  readonly query = signal<string>('');
  readonly loading = signal<boolean>(true);
  readonly items = signal<readonly Ticket[]>([]);

  constructor() {
    const destroyRef = inject(DestroyRef);

    toObservable(this.query)
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => this.loading.set(true)),
        switchMap((q) =>
          this.tickets.list({ q: q.trim() || undefined, size: 100 }).pipe(
            catchError(() => of({ content: [] as Ticket[], page: 0, size: 0, totalElements: 0, totalPages: 0 }))
          )
        ),
        takeUntilDestroyed(destroyRef)
      )
      .subscribe((response) => {
        this.items.set(response.content);
        this.loading.set(false);
      });
  }

  onSearch(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.query.set(value);
  }

  channelLabel(channel: TicketChannel): string {
    return CHANNEL_LABELS[channel];
  }

  open(id: string): void {
    void this.router.navigate(['/tickets', id]);
  }

  trackById(_: number, ticket: Ticket): string {
    return ticket.id;
  }
}
