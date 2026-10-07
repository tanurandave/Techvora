# Techvora Architecture

Techvora is a production-grade, SEO-first developer learning platform.

## High-Level Architecture

The platform is split into two primary, independently deployable services within a monorepo structure:
1. **Frontend**: Next.js App Router (React Server Components), TypeScript, Tailwind CSS, shadcn/ui.
2. **Backend**: Java 21 LTS, Spring Boot, Spring Security (JWT + RBAC), Spring Data JPA, PostgreSQL.

## Clean Architecture (Backend)

The backend strictly adheres to Clean Architecture principles, ensuring separation of concerns:
- **Controllers**: Thin presentation layer that handles HTTP requests and input validation.
- **DTOs**: Data Transfer Objects used strictly for API contracts (Request/Response).
- **Services**: Contains the core business logic.
- **Repositories**: Interfaces extending `JpaRepository` for data access.
- **Entities**: JPA models mapped to PostgreSQL tables.
- **Exceptions**: Centralized `@RestControllerAdvice` (`GlobalExceptionHandler`) to standardize error responses into a consistent `ApiResponse<T>` wrapper.

## Security & RBAC

- **Authentication**: Stateless JWT-based authentication.
- **Roles**: `SUPER_ADMIN`, `ADMIN`, `EDITOR`, `AUTHOR`, `USER`.
- **Authorization**: Enforced on the backend via `@PreAuthorize` annotations on controller methods.

## SEO & Content Management

- **Slug Management**: The backend is the ultimate source of truth for generating URL-safe, unique slugs.
- **Slug History**: `ArticleSlugHistory` tracks slug changes to ensure permanent 301/308 redirects, preserving SEO juice.
- **Metadata**: Next.js dynamically generates SEO metadata (OpenGraph, Twitter Cards).
- **Sitemap & Robots**: Dynamically generated via `app/sitemap.ts` and `app/robots.ts`.
- **JSON-LD**: Schema.org `Article` data is injected directly into the DOM of article pages.

## DevOps & Deployment

- **Containerization**: Both frontend and backend include multi-stage `Dockerfile`s optimized for production.
- **CI/CD**: GitHub Actions workflow (`main.yml`) runs automated tests, linting, and builds the Docker images.
- **Database**: PostgreSQL schema initialization is handled via standard JPA auto-generation (`update`), intended to be replaced with Flyway/Liquibase in production.
