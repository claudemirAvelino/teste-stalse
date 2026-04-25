import { ChangeDetectionStrategy, Component, Input, signal } from '@angular/core';

import { CategoryCount } from '../../../core/models/metrics.model';

@Component({
  selector: 'app-top-categories',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="space-y-3">
      @for (item of items(); track item.category) {
        <li>
          <div class="flex items-center justify-between text-sm">
            <span class="font-medium text-slate-700">{{ item.category }}</span>
            <span class="tabular-nums text-slate-500">{{ item.count }}</span>
          </div>
          <div
            class="mt-1 h-1.5 w-full overflow-hidden rounded-full bg-slate-100"
            role="progressbar"
            [attr.aria-valuenow]="item.count"
            [attr.aria-valuemax]="max()"
          >
            <div class="h-full rounded-full bg-brand-500" [style.width.%]="percent(item.count)"></div>
          </div>
        </li>
      } @empty {
        <li class="text-sm text-slate-500">Sem categorias para exibir.</li>
      }
    </ul>
  `
})
export class TopCategoriesComponent {
  @Input({ required: true })
  set categories(value: readonly CategoryCount[]) {
    this.items.set(value);
    const highest = value.reduce((acc, c) => Math.max(acc, c.count), 0);
    this.max.set(highest);
  }

  readonly items = signal<readonly CategoryCount[]>([]);
  readonly max = signal<number>(0);

  percent(count: number): number {
    const ceiling = this.max();
    return ceiling === 0 ? 0 : (count / ceiling) * 100;
  }
}
