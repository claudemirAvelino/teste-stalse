import { InjectionToken } from '@angular/core';

/**
 * Prefixo da API (vazio no dev — proxy.conf.json roteia /tickets e /metrics
 * para o backend; em produção o nginx faz o mesmo papel via proxy_pass).
 */
export const API_BASE_URL = new InjectionToken<string>('API_BASE_URL', {
  providedIn: 'root',
  factory: (): string => ''
});
