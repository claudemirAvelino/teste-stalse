# Stalse Mini Inbox

Desafio técnico Stalse — aplicação de gestão de tickets (inbox) com backend Spring Boot, frontend Angular e PostgreSQL.

## Stack

- **Backend:** Java 21, Spring Boot 3.3.4, Maven 4
- **Frontend:** Angular 17.3.0, TailwindCSS *(em desenvolvimento)*
- **Banco:** PostgreSQL 15
- **Infra:** Docker Compose

## Estrutura

```
teste-stalse/
├── backend/            # Spring Boot (Hexagonal / Ports & Adapters)
├── frontend/           # Angular 17 (em breve)
├── docker-compose.yml  # postgres + backend (+ frontend em breve)
└── README.md
```

## Como rodar

### Com Docker (recomendado)

Pré-requisitos: [Docker](https://docs.docker.com/get-docker/) + Docker Compose v2.

```bash
git clone https://github.com/claudemirAvelino/teste-stalse.git
cd teste-stalse
docker compose up --build
```

Serviços:

| Serviço  | URL                              | Credenciais                         |
|----------|----------------------------------|-------------------------------------|
| Backend  | http://localhost:8080            | —                                   |
| Health   | http://localhost:8080/actuator/health | —                              |
| Postgres | localhost:5432                   | db: `inbox`, user: `inbox`, pass: `inbox` |

Para parar e apagar o volume do banco:

```bash
docker compose down -v
```

Se as portas `8080` ou `5432` estiverem ocupadas no seu host, sobrescreva:

```bash
BACKEND_PORT=18080 DB_PORT=15432 docker compose up --build
```

### Sem Docker

Pré-requisitos: **Java 21**, **Maven 3.9+**, **PostgreSQL 15** rodando em `localhost:5432`.

1. Crie o banco e usuário:

   ```sql
   CREATE DATABASE inbox;
   CREATE USER inbox WITH PASSWORD 'inbox';
   GRANT ALL PRIVILEGES ON DATABASE inbox TO inbox;
   ```

2. Rode o backend (a partir da raiz):

   ```bash
   cd backend
   mvn spring-boot:run
   ```

   O Flyway cria a tabela `tickets` e popula com 20 registros na primeira execução.

## Status

Em desenvolvimento — veja histórico de commits para acompanhar progresso.
