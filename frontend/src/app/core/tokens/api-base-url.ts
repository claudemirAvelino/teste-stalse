import { InjectionToken } from '@angular/core';

/**
 * Prefixo da API. Usamos '/api' tanto em dev quanto em produção:
 * - Dev: proxy.conf.json reescreve '/api/...' → backend em http://localhost:8080
 * - Prod (Docker): nginx faz o mesmo via proxy_pass http://backend:8080/
 *
 * Isso evita colisão entre rotas da SPA (ex: /tickets) e endpoints da API.
 */
export const API_BASE_URL = new InjectionToken<string>('API_BASE_URL', {
  providedIn: 'root',
  factory: (): string => '/api'
});
