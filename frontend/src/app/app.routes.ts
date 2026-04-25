import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'tickets' },
  {
    path: 'tickets',
    loadComponent: () =>
      import('./features/tickets/list/tickets-list.page').then((m) => m.TicketsListPage)
  },
  {
    path: 'tickets/:id',
    loadComponent: () =>
      import('./features/tickets/detail/ticket-detail.page').then((m) => m.TicketDetailPage)
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard.page').then((m) => m.DashboardPage)
  }
];
