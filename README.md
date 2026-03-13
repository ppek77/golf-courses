# Golf Courses

Zajícova evidence golfových hřišť.

## Prerequisites

- Java 25
- Maven (or use the Maven wrapper `./mvnw`)
- Docker

## Running locally

The application uses Spring Boot Docker Compose support, so PostgreSQL starts automatically:

```bash
./mvnw spring-boot:run
```

This will:
1. Start a PostgreSQL 17 container (via `docker-compose.yml`)
2. Run Flyway migrations
3. Start the application on http://localhost:8080

To stop the app, press `Ctrl+C` — the database container will stop automatically.

### Running the database separately

If you prefer to manage the database container yourself:

```bash
docker compose up -d
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean install
```

## Tests

```bash
./mvnw test
```
