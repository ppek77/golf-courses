# Golf Courses Application

Web application for registering golf courses and tracking rounds played on them.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.0.3
- **Database:** PostgreSQL
- **Build tool:** Maven
- **UI:** Thymeleaf server-side templates
- **Deployment:** Docker container on DigitalOcean droplet

## Project Structure

Standard Maven/Spring Boot layout:

```
src/main/java/          - Application source code
src/main/resources/     - Configuration, templates, static assets
src/test/java/          - Tests
```

## Build & Run

```bash
./mvnw clean install          # Build the project
./mvnw spring-boot:run        # Run locally
./mvnw test                   # Run tests
```

## Conventions

- Use `application.yml` (not `.properties`) for configuration
- Database migrations managed via Flyway (SQL scripts in `src/main/resources/db/migration/`)
- Spring MVC controllers returning Thymeleaf views
- Thymeleaf templates in `src/main/resources/templates/`
- Static assets (CSS, JS, images) in `src/main/resources/static/`
- Entity classes use JPA annotations
- Follow standard Java naming conventions (camelCase for fields/methods, PascalCase for classes)
- Package structure: `info.pekny.golfcourses` (base package)
- Write tests for all service-layer logic
- Always respect the security concerns described in the [Security Guide](features/01-Security.md)
- data model is described in the [Data Model](features/02-Data-Model.md)

## Docker

- Dockerfile in project root
- Use multi-stage build (build with Maven, run with JRE)
- `docker-compose.yml` for local development with PostgreSQL

## Database

- PostgreSQL accessed via Spring Data JPA / Hibernate
- All schema changes via Flyway migration scripts (never auto-ddl in production)
- Migration naming: `V{number}__{description}.sql`
