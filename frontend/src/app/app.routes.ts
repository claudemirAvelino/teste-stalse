import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'tickets' },
  {
    path: 'tickets',
    loadComponent: () =>
      import('./features/tickets/list/tickets-list.page').then((m) => m.TicketsListPage)
  }
];
