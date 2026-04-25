export type TicketStatus = 'OPEN' | 'IN_PROGRESS' | 'CLOSED';
export type TicketPriority = 'LOW' | 'MEDIUM' | 'HIGH';
export type TicketChannel = 'EMAIL' | 'WHATSAPP' | 'CHAT' | 'PHONE';

export const TICKET_STATUSES: readonly TicketStatus[] = ['OPEN', 'IN_PROGRESS', 'CLOSED'] as const;
export const TICKET_PRIORITIES: readonly TicketPriority[] = ['LOW', 'MEDIUM', 'HIGH'] as const;

export interface Ticket {
  readonly id: string;
  readonly customerName: string;
  readonly channel: TicketChannel;
  readonly subject: string;
  readonly description: string | null;
  readonly category: string;
  readonly status: TicketStatus;
  readonly priority: TicketPriority;
  readonly createdAt: string;
  readonly updatedAt: string;
}

export interface TicketPatch {
  readonly status?: TicketStatus;
  readonly priority?: TicketPriority;
}

export interface PageResponse<T> {
  readonly content: readonly T[];
  readonly page: number;
  readonly size: number;
  readonly totalElements: number;
  readonly totalPages: number;
}
