import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-metric-card',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="rounded-xl border border-slate-200 bg-white p-5 shadow-sm">
      <div class="text-xs font-medium uppercase tracking-wider text-slate-500">{{ label }}</div>
      <div class="mt-2 flex items-baseline gap-2">
        <span class="text-3xl font-semibold text-slate-900 tabular-nums">{{ value }}</span>
        @if (hint) {
          <span class="text-xs text-slate-500">{{ hint }}</span>
        }
      </div>
    </div>
  `
})
export class MetricCardComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) value!: number;
  @Input() hint: string = '';
}
