import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

import { ApiError } from '../models/api-error.model';
import { ErrorBus } from './error-bus';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const bus = inject(ErrorBus);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      const apiError = toApiError(err);
      bus.emit(apiError);
      return throwError(() => apiError);
    })
  );
};

function toApiError(err: HttpErrorResponse): ApiError {
  const body = err.error as Partial<ApiError> | null;
  return {
    status: err.status,
    title: body?.title ?? err.statusText ?? 'Network error',
    detail: body?.detail ?? err.message,
    errors: body?.errors
  };
}
