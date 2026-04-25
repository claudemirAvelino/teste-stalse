import { Injectable, signal } from '@angular/core';

import { ApiError } from '../models/api-error.model';

/**
 * Bus simples para erros HTTP. Componentes podem ler `last()` ou
 * inscrever-se ao signal para mostrar toasts/inline messages.
 */
@Injectable({ providedIn: 'root' })
export class ErrorBus {
  private readonly _last = signal<ApiError | null>(null);

  readonly last = this._last.asReadonly();

  emit(error: ApiError): void {
    this._last.set(error);
  }

  clear(): void {
    this._last.set(null);
  }
}
