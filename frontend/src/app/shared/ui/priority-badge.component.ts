import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

import { TicketPriority } from '../../core/models/ticket.model';

const LABELS: Record<TicketPriority, string> = {
  LOW: 'Baixa',
  MEDIUM: 'Média',
  HIGH: 'Alta'
};

const CLASSES: Record<TicketPriority, string> = {
  LOW: 'bg-slate-50 text-slate-600 ring-slate-500/20',
  MEDIUM: 'bg-blue-50 text-blue-700 ring-blue-600/20',
  HIGH: 'bg-rose-50 text-rose-700 ring-rose-600/20'
};

@Component({
  selector: 'app-priority-badge',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span
      class="inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium ring-1 ring-inset"
      [class]="badgeClass"
    >{{ label }}</span>
  `
})
export class PriorityBadgeComponent {
  @Input({ required: true }) priority!: TicketPriority;

  get label(): string {
    return LABELS[this.priority];
  }

  get badgeClass(): string {
    return CLASSES[this.priority];
  }
}
