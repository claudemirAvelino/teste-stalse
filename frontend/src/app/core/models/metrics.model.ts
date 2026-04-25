import { TicketStatus } from './ticket.model';

export interface CategoryCount {
  readonly category: string;
  readonly count: number;
}

export interface DailyCount {
  readonly date: string;
  readonly count: number;
}

export interface Metrics {
  readonly byStatus: Readonly<Record<TicketStatus, number>>;
  readonly topCategories: readonly CategoryCount[];
  readonly dailyEvolution: readonly DailyCount[];
}
