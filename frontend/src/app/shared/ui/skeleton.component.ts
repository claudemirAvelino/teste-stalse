import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-skeleton',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      class="animate-pulse rounded-md bg-slate-200/70"
      [style.height]="height"
      [style.width]="width"
    ></div>
  `
})
export class SkeletonComponent {
  @Input() height: string = '1rem';
  @Input() width: string = '100%';
}
