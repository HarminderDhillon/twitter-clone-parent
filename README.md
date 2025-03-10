# Twitter Clone - Backend API

This is the backend component of the Twitter Clone project, providing RESTful API endpoints using Spring Boot.

## Project Overview

This repository contains only the backend API for the Twitter Clone application. The frontend has been moved to a separate repository/directory (`twitter-clone-ui`). The parent project that coordinates both components can be found at [twitter-clone-parent](https://github.com/HarminderDhillon/twitter-clone-parent).

## Project Structure

The project follows standard Spring Boot project structure:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── dhillon/
│   │           └── twitterclone/
│   │               ├── config/        # Configuration classes
│   │               ├── controller/    # REST API controllers
│   │               ├── dto/           # Data Transfer Objects
│   │               ├── entity/        # JPA entity classes
│   │               ├── exception/     # Custom exceptions
│   │               ├── repository/    # Spring Data repositories
│   │               ├── security/      # Security configurations
│   │               ├── service/       # Business logic
│   │               └── util/          # Utility classes
│   └── resources/
│       ├── application.yml           # Application configuration
│       ├── db/
│       │   └── changelog/            # Liquibase changelog files
│       └── static/                   # Static resources
└── test/
    └── java/                        # Test classes
```

## Getting Started

### Prerequisites

- JDK 21 or later
- Maven 3.8 or later (or use the included Maven Wrapper)
- PostgreSQL 14 or later
- Redis 6.2 or later (optional for development)
- Elasticsearch 8.x (optional for development)
- RabbitMQ 3.9 or later (optional for development)

### Running the Backend Standalone

1. Clone the repository
```bash
git clone https://github.com/HarminderDhillon/twitter-clone.git
cd twitter-clone
```

2. Start required services (PostgreSQL is required, others are optional)
```bash
# Using Docker (recommended)
docker run -d --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=twitterclone -p 5432:5432 postgres:14
```

3. Run the application
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8081/api`

### Running with the Frontend

For running both the frontend and backend together, please refer to the parent project at [twitter-clone-parent](https://github.com/HarminderDhillon/twitter-clone-parent).

## Environment Profiles

The application supports multiple environments:

- `dev` - Development environment (default)
- `test` - Testing environment
- `prod` - Production environment

To run with a specific profile, use:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: Interactive API documentation
  ```
  http://localhost:8081/api/swagger-ui.html
  ```

- **OpenAPI Specification**: Raw OpenAPI JSON
  ```
  http://localhost:8081/api/api-docs
  ```

## Testing

```bash
# Run all tests
./mvnw test

# Run specific test categories
./mvnw test -Dtest=*ControllerTest      # Run controller tests
./mvnw test -Dtest=*ServiceTest         # Run service tests
./mvnw test -Dtest=*IntegrationTest     # Run integration tests

# Run E2E tests (using Testcontainers)
./mvnw test -Dtest=*E2ETest
```

## Technology Stack

- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Cache**: Redis for session management and caching
- **Search**: Elasticsearch for advanced search capabilities
- **Security**: JWT-based authentication with Spring Security
- **Messaging**: WebSockets with STOMP for real-time features
- **API Documentation**: SpringDoc with OpenAPI (Swagger)
- **Migration**: Liquibase for database schema migrations

## Features

- User Management: Registration, authentication, profiles, follows
- Posts: Create, reply, repost, and like posts
- Hashtags: Automatic detection and indexing
- Timeline: Home timeline, user timeline, and explore feed
- Notifications: Real-time notifications
- Search: Find users, posts, and hashtags
- Direct Messaging: One-on-one and group chats

## License

This project is open source and available under the [MIT License](LICENSE).

## Database Migrations

### Liquibase Migration

The application uses Liquibase for database schema migrations. Liquibase offers several advantages over the previously used Flyway:

1. **Multiple Format Support**: Supports XML, YAML, JSON, and SQL formats
2. **Enhanced Rollback**: Better support for rollback operations
3. **Preconditions**: Ability to specify preconditions for changesets
4. **Contexts**: Support for running different changes in different environments
5. **SQL Generation**: Ability to generate SQL scripts from changesets for DBA review

### Migration Structure

The migration files are organized as follows:

- `db.changelog-master.yaml`: The master changelog file that includes all other changesets
- `changes/`: Directory containing YAML-based changesets
- `sql/`: Directory containing SQL-based migrations

### Running Migrations

Migrations are automatically applied when the application starts. The configuration in `application.yml` controls this behavior:

```yaml
spring:
  liquibase:
    enabled: true
    change-log: classpath:db/changelog/db.changelog-master.yaml
    database-change-log-table: DATABASECHANGELOG
    database-change-log-lock-table: DATABASECHANGELOGLOCK
```

For different environments, the migration behavior can be customized:

- **Development**: Migrations are disabled by default to allow Hibernate to update the schema
- **Test**: Migrations are enabled to ensure consistent test data
- **Production**: Migrations are enabled to ensure controlled schema changes

### Adding New Migrations

To add a new migration:

1. Create a new changeset file in the `changes/` directory or a new SQL file in the `sql/` directory
2. Add the file to the master changelog (`db.changelog-master.yaml`)
3. Run the application to apply the migration 

## Recent Updates

### API Endpoint Improvements (March 2025)

We've made significant improvements to the API endpoints, focusing especially on the user management functionality:

1. **Fixed Ambiguous URL Mappings**: Resolved issues with conflicting request mappings in the UserController. The application now correctly handles both UUID-based and username-based user lookups.

2. **Improved Error Handling**: Enhanced error responses with detailed error messages and appropriate HTTP status codes.

3. **Added Follower Count Integration**: Updated the API to accurately retrieve follower and following counts using the FollowRepository.

4. **Username and Email Availability Checks**: Added dedicated endpoints to check if a username or email is already in use.

5. **Consolidated Controller Logic**: Simplified the controller by combining similar methods and improving code organization.

All API endpoints now have consistent behavior and properly follow RESTful principles.

### API Endpoints Testing Status

All endpoints have been tested and are working correctly:

- ✅ GET `/api/users` - Get all users
- ✅ GET `/api/users/{idOrUsername}` - Get user by ID or username
- ✅ PUT `/api/users/{idOrUsername}` - Update user
- ✅ DELETE `/api/users/{idOrUsername}` - Delete user
- ✅ POST `/api/users` - Create new user
- ✅ GET `/api/users/check-username?username={username}` - Check username availability
- ✅ GET `/api/users/check-email?email={email}` - Check email availability
- ✅ GET `/api/users/search?query={query}` - Search users 