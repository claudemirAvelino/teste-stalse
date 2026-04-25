import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../../../core/tokens/api-base-url';
import { PageResponse, Ticket, TicketPatch } from '../../../core/models/ticket.model';

export interface ListTicketsParams {
  readonly q?: string;
  readonly page?: number;
  readonly size?: number;
}

@Injectable({ providedIn: 'root' })
export class TicketsService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = inject(API_BASE_URL);

  list(params: ListTicketsParams = {}): Observable<PageResponse<Ticket>> {
    let httpParams = new HttpParams();
    if (params.q) httpParams = httpParams.set('q', params.q);
    if (params.page !== undefined) httpParams = httpParams.set('page', params.page);
    if (params.size !== undefined) httpParams = httpParams.set('size', params.size);

    return this.http.get<PageResponse<Ticket>>(`${this.baseUrl}/tickets`, { params: httpParams });
  }

  getById(id: string): Observable<Ticket> {
    return this.http.get<Ticket>(`${this.baseUrl}/tickets/${id}`);
  }

  update(id: string, patch: TicketPatch): Observable<Ticket> {
    return this.http.patch<Ticket>(`${this.baseUrl}/tickets/${id}`, patch);
  }
}
