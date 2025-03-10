# Twitter Clone Application

This is a full-stack Twitter clone application with a Spring Boot backend and Next.js frontend.

## Project Structure

- `backend/` - Spring Boot backend (REST API)
- `frontend/` - Next.js frontend

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
git clone https://github.com/HarminderDhillon/twitter-clone-parent.git
cd twitter-clone-parent
```

### 2. Setup Workspace

The simplest way to set up the workspace is to run the setup script:

```bash
./setup-workspace.sh
```

This will:
- Install root-level dependencies
- Install frontend dependencies
- Download backend dependencies (if Maven is available)
- Optionally start required Docker services

Alternatively, you can manually set up the workspace:

```bash
# Install root dependencies
npm install

# Install frontend dependencies
npm install --workspace=frontend

# Install backend dependencies
cd backend && ./mvnw dependency:go-offline -B && cd ..
```

### 3. Start Required Services

The application requires PostgreSQL to be running. You have two options:

**Option 1:** Use npm script (recommended)
```bash
npm run docker
```

**Option 2:** Use Docker Compose directly
```bash
docker-compose up -d postgres redis
```

**Option 3:** Use locally installed PostgreSQL
```bash
# On macOS with Homebrew
brew services start postgresql

# On Ubuntu/Debian
sudo service postgresql start
```

Ensure you have a database named `twitterclone` with username `postgres` and password `postgres`.

### 4. Run the Application

You can run both the frontend and backend with a single command:

```bash
npm start
```

Or you can run them separately:

**Backend:**
```bash
npm run start:backend
```

**Frontend:**
```bash
npm run start:frontend
```

### 5. Access the Application

- Frontend: http://localhost:3000
- Backend API: http://localhost:8081/api
- API Documentation: http://localhost:8081/api/swagger-ui.html

## Docker Setup

### Run with Docker Compose

The entire application can be run using Docker Compose:

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Backend Dockerization

The Spring Boot backend has been dockerized. See `backend/README.docker.md` for detailed instructions.

This setup includes:
- Multi-stage build for efficient Docker images
- Proper layering for better cache utilization
- Environment variable configuration
- Healthchecks for dependency services
- Non-root user for improved security

### Frontend Dockerization

The Next.js frontend has been dockerized with the following features:
- Multi-stage build process for minimal image size
- Production-ready configuration
- Automatic environment configuration for API communication
- Seamless integration with the backend service

### Available Services

The following services are configured in Docker Compose:
- PostgreSQL database
- Redis for caching
- Elasticsearch for search functionality
- RabbitMQ for messaging
- Spring Boot backend API (available at http://localhost:8082/api when running in Docker)
- Next.js frontend (available at http://localhost:3000 when running in Docker)

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
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Database Migrations

Database migrations are managed with Liquibase:

```bash
# To create a new migration (change set)
cd backend
./mvnw liquibase:generateChangeLog
```

## Testing

```bash
# Run backend tests
cd backend
./mvnw test

# Run frontend tests
cd frontend
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
   npm run docker -- postgres
   ```

## Available Scripts

The project includes several npm scripts to help with development:

- `npm start` - Starts both backend and frontend
- `npm run build` - Builds both backend and frontend
- `npm run test` - Runs tests for both backend and frontend
- `npm run docker` - Starts all Docker services
- `npm run docker:build` - Builds all Docker images
- `npm run docker:down` - Stops all Docker services
- `npm run docker:logs` - Shows logs from all Docker services
- `npm run lint` - Runs linting on frontend code
- `npm run clean` - Cleans build artifacts and dependencies

```bash
npm run docker
```

```bash
npm run docker:build
```

```bash
npm run docker:down
```

```bash
npm run docker:logs
```

```bash
npm run lint
```

```bash
npm run clean
``` 