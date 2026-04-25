import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';

import { DailyCount } from '../../../core/models/metrics.model';

Chart.register(...registerables);

@Component({
  selector: 'app-daily-chart',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [NgChartsModule],
  template: `
    <canvas baseChart [type]="'line'" [data]="data" [options]="options"></canvas>
  `
})
export class DailyChartComponent {
  @Input({ required: true })
  set series(value: readonly DailyCount[]) {
    this.data = {
      labels: value.map((d) => formatLabel(d.date)),
      datasets: [
        {
          data: value.map((d) => d.count),
          label: 'Tickets criados',
          borderColor: '#f97316',
          backgroundColor: 'rgba(249, 115, 22, 0.12)',
          pointBackgroundColor: '#f97316',
          pointRadius: 4,
          tension: 0.3,
          fill: true
        }
      ]
    };
  }

  data: ChartConfiguration<'line'>['data'] = { labels: [], datasets: [] };

  readonly options: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: { intersect: false, mode: 'index' }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: { precision: 0 }
      },
      x: {
        grid: { display: false }
      }
    }
  };
}

function formatLabel(isoDate: string): string {
  const [, month, day] = isoDate.split('-');
  return `${day}/${month}`;
}
