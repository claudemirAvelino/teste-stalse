# Stalse Mini Inbox

Aplicação de gestão de tickets (mini inbox) para o desafio técnico da Stalse.
Backend Java 21 + Spring Boot, frontend Angular 17 + Tailwind, PostgreSQL 15,
tudo orquestrado via Docker Compose.

## Stack

| Camada    | Tecnologia                                                              |
|-----------|-------------------------------------------------------------------------|
| Backend   | Java 21, Spring Boot 3.3.4, Spring Data JPA, Flyway 10, Spring Retry    |
| Frontend  | Angular 17.3.0 (Standalone + Signals), TailwindCSS 3.4, ng2-charts/Chart.js |
| Banco     | PostgreSQL 15                                                           |
| Infra     | Docker Compose, nginx (servindo o build de produção)                    |

## Como rodar

### Com Docker (recomendado)

Pré-requisito: [Docker](https://docs.docker.com/get-docker/) com Docker Compose v2.

```bash
git clone https://github.com/claudemirAvelino/teste-stalse.git
cd teste-stalse
docker compose up --build
```

| Serviço  | URL                                   | Credenciais                               |
|----------|---------------------------------------|-------------------------------------------|
| Frontend | http://localhost:4200                 | —                                         |
| API      | http://localhost:4200/api             | (proxy do nginx para o backend)           |
| Backend  | http://localhost:8080                 | —                                         |
| Health   | http://localhost:8080/actuator/health | —                                         |
| Postgres | localhost:5432                        | db `inbox`, user `inbox`, pass `inbox`    |

Para parar e remover o volume do banco:

```bash
docker compose down -v
```

Se as portas `4200`, `8080` ou `5432` estiverem ocupadas no host:

```bash
FRONTEND_PORT=14200 BACKEND_PORT=18080 DB_PORT=15432 docker compose up --build
```

### Sem Docker

Pré-requisitos: **Java 21**, **Maven 3.9+**, **Node 20**, **PostgreSQL 15** rodando em `localhost:5432`.

1. Crie o banco e o usuário:
   ```sql
   CREATE DATABASE inbox;
   CREATE USER inbox WITH PASSWORD 'inbox';
   GRANT ALL PRIVILEGES ON DATABASE inbox TO inbox;
   ```
2. Backend (cria schema e popula 20 tickets via Flyway na primeira subida):
   ```bash
   cd backend
   mvn spring-boot:run
   ```
3. Frontend (em outro terminal):
   ```bash
   cd frontend
   npm ci
   npm start
   ```
   Acesse http://localhost:4200 — o `proxy.conf.json` redireciona `/api/*` para `http://localhost:8080`.

## Endpoints

A API está exposta em `/api/*` (via nginx no Docker, via `proxy.conf.json` em dev).
Os exemplos abaixo usam `localhost:8080` direto para clareza.

### `GET /tickets`

Lista paginada com busca textual (cliente, assunto ou categoria).

```bash
curl "http://localhost:8080/tickets?q=alice&page=0&size=20"
```

```json
{
  "content": [
    {
      "id": "7514ad4f-...",
      "customerName": "Alice Souza",
      "channel": "EMAIL",
      "subject": "Cobrança duplicada em abril",
      "description": "Fatura veio com dois débitos iguais.",
      "category": "billing",
      "status": "OPEN",
      "priority": "HIGH",
      "createdAt": "2026-04-19T14:30:00Z",
      "updatedAt": "2026-04-19T14:30:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

### `GET /tickets/{id}`

Retorna um ticket. `404 + Problem Details` se não existir.

### `PATCH /tickets/{id}`

Aceita **apenas** `status` e `priority` (pelo menos um).

```bash
curl -X PATCH "http://localhost:8080/tickets/{id}" \
  -H "Content-Type: application/json" \
  -d '{"status":"CLOSED","priority":"HIGH"}'
```

| Caso                                     | Status | Resposta              |
|------------------------------------------|--------|-----------------------|
| Sucesso                                  | 200    | Ticket atualizado     |
| Body vazio (`{}`)                        | 400    | `at least one of...`  |
| Enum inválido (ex: `"ABACAXI"`)          | 400    | `Malformed body`      |
| Campo desconhecido (ex: `{"foo":"x"}`)   | 400    | `Malformed body`      |
| ID inexistente                           | 404    | `Ticket not found`    |

**Webhook assíncrono:** se a mudança resultar em `status=CLOSED` ou `priority=HIGH`,
um POST async é disparado para `https://httpbin.org/post`. Confira nos logs:

```bash
docker compose logs backend | grep -i webhook
# Dispatching webhook for ticket ... (status=CLOSED, priority=MEDIUM)
# Webhook delivered for ticket ...
```

### `GET /metrics`

Agregações para o dashboard (default `?days=7`, máx `90`).

```json
{
  "byStatus": { "OPEN": 8, "IN_PROGRESS": 5, "CLOSED": 7 },
  "topCategories": [
    { "category": "billing", "count": 5 },
    { "category": "bug",     "count": 4 }
  ],
  "dailyEvolution": [
    { "date": "2026-04-18", "count": 2 },
    { "date": "2026-04-19", "count": 3 }
  ]
}
```

Dias sem tickets aparecem com `count: 0`. Status sem tickets aparecem com `0`.

## Estrutura

```
teste-stalse/
├── backend/                 Spring Boot (Hexagonal / Ports & Adapters)
│   └── src/main/java/com/stalse/inbox/
│       ├── domain/          → modelo puro (Ticket, enums, ports, eventos)
│       ├── application/     → use cases (List, Get, Update, ComputeMetrics)
│       └── infrastructure/  → adapters (JPA, Web, Webhook HTTP)
├── frontend/                Angular 17 standalone
│   └── src/app/
│       ├── core/            → models, http (interceptor), tokens
│       ├── shared/ui/       → StatusBadge, PriorityBadge, Card, Skeleton
│       └── features/
│           ├── tickets/     → list, detail (+ TicketsService)
│           └── dashboard/   → page, components (+ MetricsService)
├── docker-compose.yml       db + backend + frontend
└── README.md
```

## Decisões de arquitetura

- **Backend Hexagonal pragmática** (3 camadas, sem ritual DDD pleno). Domain
  puro, sem JPA/Spring; ports declarados em `domain/port/`; adapters em
  `infrastructure/`. 26 testes JUnit 5 + Mockito cobrem regras de transição,
  use cases e o webhook listener.
- **Webhook async via `ApplicationEvents` com `@TransactionalEventListener
  (AFTER_COMMIT)` + `@Async`** em pool dedicado (`webhookExecutor`). Dispara
  só após commit (evita notificar update revertido), retenta 3× via
  `@Retryable` e o `@Recover` apenas loga o erro final (sem DLQ — não vale
  para uma única notificação a httpbin).
- **Flyway** para schema (`V1`) e seed (`V2`, 20 tickets distribuídos em 7 dias,
  7 categorias, 3 status, 3 prioridades) — versionado, idempotente e roda
  igual em dev/CI/Docker.
- **Frontend Signals + Standalone Components**, lazy load por rota,
  `ChangeDetectionStrategy.OnPush` em todos os componentes.
- **PATCH otimista** na página de detalhe (atualiza UI antes do request,
  reverte em erro, indicador "Salvando → Salvo").
- **TailwindCSS** em vez de Angular Material (mais liberdade visual em 5
  dias; identidade B2B com paleta brand customizada).
- **Prefixo `/api`** unificado em dev (proxy.conf.json com `pathRewrite`)
  e produção (nginx `proxy_pass http://backend:8080/`) — evita colisão
  entre rotas da SPA (ex: `/tickets`) e endpoints da API.

## Testes

```bash
# Backend (26 testes JUnit + Mockito)
cd backend && mvn test

# Frontend (lint estrito)
cd frontend && npm run lint
```

Para validar o `mvn test` sem instalar Maven local, use o container oficial:

```bash
docker run --rm -v "$PWD/backend":/app -w /app maven:3.9-eclipse-temurin-21 mvn -B test
```

## Troubleshooting

- **Portas ocupadas**: use `FRONTEND_PORT`, `BACKEND_PORT`, `DB_PORT` no
  `docker compose up`.
- **Volumes antigos** com schema diferente após reset: `docker compose down -v`.
- **Maven 4**: o desafio pede "Maven 4.0.0" — interpretado como
  `<modelVersion>4.0.0</modelVersion>` (formato XML do POM, presente há anos),
  não o binário Maven 4 (ainda em RC). Build validado com Maven 3.9.x +
  Java 21.
