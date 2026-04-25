import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

import { TicketStatus } from '../../core/models/ticket.model';

const LABELS: Record<TicketStatus, string> = {
  OPEN: 'Aberto',
  IN_PROGRESS: 'Em andamento',
  CLOSED: 'Fechado'
};

const CLASSES: Record<TicketStatus, string> = {
  OPEN: 'bg-emerald-50 text-emerald-700 ring-emerald-600/20',
  IN_PROGRESS: 'bg-amber-50 text-amber-700 ring-amber-600/20',
  CLOSED: 'bg-slate-100 text-slate-600 ring-slate-500/20'
};

@Component({
  selector: 'app-status-badge',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      class="inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium ring-1 ring-inset"
      [class]="badgeClass"
    >{{ label }}</span>
  `
})
export class StatusBadgeComponent {
  @Input({ required: true }) status!: TicketStatus;

  get label(): string {
    return LABELS[this.status];
  }

  get badgeClass(): string {
    return CLASSES[this.status];
  }
}
