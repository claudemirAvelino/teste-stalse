import { ChangeDetectionStrategy, Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { EMPTY, catchError } from 'rxjs';

import { Metrics } from '../../core/models/metrics.model';
import { CardComponent } from '../../shared/ui/card.component';
import { SkeletonComponent } from '../../shared/ui/skeleton.component';
import { DailyChartComponent } from './components/daily-chart.component';
import { MetricCardComponent } from './components/metric-card.component';
import { TopCategoriesComponent } from './components/top-categories.component';
import { MetricsService } from './data/metrics.service';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CardComponent,
    SkeletonComponent,
    DailyChartComponent,
    MetricCardComponent,
    TopCategoriesComponent
  ],
  templateUrl: './dashboard.page.html'
})
export class DashboardPage {
  private readonly metrics = inject(MetricsService);
  private readonly destroyRef = inject(DestroyRef);

  readonly data = signal<Metrics | null>(null);
  readonly loading = signal<boolean>(true);

  constructor() {
    this.metrics
      .get(7)
      .pipe(
        catchError(() => {
          this.loading.set(false);
          return EMPTY;
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((metrics) => {
        this.data.set(metrics);
        this.loading.set(false);
      });
  }

  total(metrics: Metrics): number {
    return Object.values(metrics.byStatus).reduce((sum, v) => sum + v, 0);
  }
}
