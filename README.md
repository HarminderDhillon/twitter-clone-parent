# Twitter Clone Application

This is a full-stack Twitter clone application with a Spring Boot backend and Next.js frontend.

## Project Structure

- `twitter-clone/` - Spring Boot backend (REST API)
- `twitter-clone-ui/` - Next.js frontend

## Prerequisites

- Java 21 or higher
- Maven 3.8 or higher (or use the included Maven Wrapper)
- Node.js 18 or higher
- npm or yarn
- PostgreSQL 14 or higher
- Redis (optional, for caching)
- Docker and Docker Compose (optional, for running services)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd twitter-clone-application
```

### 2. Start Required Services

The application requires PostgreSQL to be running. You have two options:

**Option 1:** Use Docker Compose (recommended)
```bash
docker-compose up -d postgres redis
```

**Option 2:** Use locally installed PostgreSQL
```bash
# On macOS with Homebrew
brew services start postgresql

# On Ubuntu/Debian
sudo service postgresql start
```

Ensure you have a database named `twitterclone` with username `postgres` and password `postgres`.

### 3. Run the Application

You can run both the frontend and backend with a single script:

```bash
./start-apps.sh
```

This script will:
- Check if PostgreSQL is running and prompt to start it if needed
- Start the Spring Boot backend on port 8081
- Start the Next.js frontend on port 3000
- Use tmux to display both applications in a split terminal (if tmux is available)

Or you can run them separately:

**Backend:**
```bash
cd twitter-clone
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd twitter-clone-ui
npm install
npm run dev
```

### 4. Access the Application

- Frontend: http://localhost:3000
- Backend API: http://localhost:8081/api
- API Documentation: http://localhost:8081/api/swagger-ui.html

## Development

### Backend

The Spring Boot backend follows standard Spring Boot project structure:

- `src/main/java` - Java source code
- `src/main/resources` - Configuration files
- `src/test/java` - Unit and integration tests

### Frontend

The Next.js frontend follows standard Next.js project structure:

- `app/` - Next.js App Router components
- `components/` - Reusable UI components
- `services/` - API service layer
- `public/` - Static assets

## Environment Profiles

The application supports multiple environments:

- `dev` - Development environment (default)
- `test` - Testing environment
- `prod` - Production environment

To run with a specific profile, use:

```bash
cd twitter-clone
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Database Migrations

Database migrations are managed with Liquibase:

```bash
# To create a new migration (change set)
cd twitter-clone
./mvnw liquibase:generateChangeLog
```

## Testing

```bash
# Run backend tests
cd twitter-clone
./mvnw test

# Run frontend tests
cd twitter-clone-ui
npm test
```

## Troubleshooting

### Next.js Installation Issues

If you encounter issues with the Next.js frontend, you can fix them using:

```bash
./fix-nextjs.sh
```

This script will:
- Clean the Next.js installation
- Install a stable version of Next.js
- Reinstall all dependencies

### PostgreSQL Connection Issues

If the backend can't connect to PostgreSQL:

1. Ensure PostgreSQL is running:
   ```bash
   pg_isready -h localhost -p 5432
   ```

2. Start PostgreSQL using Docker if you don't have it installed locally:
   ```bash
   docker-compose up -d postgres
   ``` 