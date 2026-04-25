import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../../../core/tokens/api-base-url';
import { Metrics } from '../../../core/models/metrics.model';

@Injectable({ providedIn: 'root' })
export class MetricsService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = inject(API_BASE_URL);

  get(days: number = 7): Observable<Metrics> {
    const params = new HttpParams().set('days', days);
    return this.http.get<Metrics>(`${this.baseUrl}/metrics`, { params });
  }
}
