import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-card',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="rounded-xl border border-slate-200 bg-white shadow-sm">
      <ng-content />
    </div>
  `
})
export class CardComponent {}
