import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  effect,
  inject,
  input,
  signal
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { EMPTY, catchError } from 'rxjs';

import {
  TICKET_PRIORITIES,
  TICKET_STATUSES,
  Ticket,
  TicketChannel,
  TicketPatch,
  TicketPriority,
  TicketStatus
} from '../../../core/models/ticket.model';
import { CardComponent } from '../../../shared/ui/card.component';
import { EmptyStateComponent } from '../../../shared/ui/empty-state.component';
import { PriorityBadgeComponent } from '../../../shared/ui/priority-badge.component';
import { SkeletonComponent } from '../../../shared/ui/skeleton.component';
import { StatusBadgeComponent } from '../../../shared/ui/status-badge.component';
import { TicketsService } from '../data/tickets.service';

const STATUS_LABELS: Record<TicketStatus, string> = {
  OPEN: 'Aberto',
  IN_PROGRESS: 'Em andamento',
  CLOSED: 'Fechado'
};

const PRIORITY_LABELS: Record<TicketPriority, string> = {
  LOW: 'Baixa',
  MEDIUM: 'Média',
  HIGH: 'Alta'
};

const CHANNEL_LABELS: Record<TicketChannel, string> = {
  EMAIL: 'E-mail',
  WHATSAPP: 'WhatsApp',
  CHAT: 'Chat',
  PHONE: 'Telefone'
};

const SAVED_FEEDBACK_MS = 2000;

@Component({
  selector: 'app-ticket-detail-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    DatePipe,
    RouterLink,
    CardComponent,
    EmptyStateComponent,
    PriorityBadgeComponent,
    SkeletonComponent,
    StatusBadgeComponent
  ],
  templateUrl: './ticket-detail.page.html'
})
export class TicketDetailPage {
  readonly id = input.required<string>();

  private readonly tickets = inject(TicketsService);
  private readonly destroyRef = inject(DestroyRef);

  readonly ticket = signal<Ticket | null>(null);
  readonly loading = signal<boolean>(true);
  readonly notFound = signal<boolean>(false);
  readonly saving = signal<boolean>(false);
  readonly justSaved = signal<boolean>(false);

  readonly statuses = TICKET_STATUSES;
  readonly priorities = TICKET_PRIORITIES;

  constructor() {
    effect(
      () => {
        const id = this.id();
        this.load(id);
      },
      { allowSignalWrites: true }
    );
  }

  statusLabel(status: TicketStatus): string {
    return STATUS_LABELS[status];
  }

  priorityLabel(priority: TicketPriority): string {
    return PRIORITY_LABELS[priority];
  }

  channelLabel(channel: TicketChannel): string {
    return CHANNEL_LABELS[channel];
  }

  onStatusChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as TicketStatus;
    this.applyOptimisticPatch({ status: value });
  }

  onPriorityChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as TicketPriority;
    this.applyOptimisticPatch({ priority: value });
  }

  private load(id: string): void {
    this.loading.set(true);
    this.notFound.set(false);
    this.ticket.set(null);

    this.tickets
      .getById(id)
      .pipe(
        catchError((err: { status?: number }) => {
          if (err?.status === 404) {
            this.notFound.set(true);
          }
          this.loading.set(false);
          return EMPTY;
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((ticket) => {
        this.ticket.set(ticket);
        this.loading.set(false);
      });
  }

  private applyOptimisticPatch(patch: TicketPatch): void {
    const current = this.ticket();
    if (!current) return;
    if (patch.status === current.status) return;
    if (patch.priority === current.priority) return;

    const optimistic: Ticket = { ...current, ...patch };
    this.ticket.set(optimistic);
    this.saving.set(true);
    this.justSaved.set(false);

    this.tickets
      .update(current.id, patch)
      .pipe(
        catchError(() => {
          this.ticket.set(current);
          this.saving.set(false);
          return EMPTY;
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((updated) => {
        this.ticket.set(updated);
        this.saving.set(false);
        this.justSaved.set(true);
        setTimeout(() => this.justSaved.set(false), SAVED_FEEDBACK_MS);
      });
  }
}
