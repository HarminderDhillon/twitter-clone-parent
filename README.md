# Twitter Clone Application

This is a full-stack Twitter clone application with a Spring Boot backend and Next.js frontend.

## Project Structure

- `backend/` - Spring Boot backend (REST API)
- `frontend/` - Next.js frontend

## Prerequisites

- Docker and Docker Compose
- npm (to run the setup script and convenience commands)

No need to install Java, Maven, Node.js, PostgreSQL or other dependencies locally - everything runs in Docker!

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
- Check for Docker and Docker Compose installation
- Install minimal npm dependencies for convenience scripts
- Optionally build the Docker images

### 3. Start the Application

Start all services with a single command:

```bash
npm start
```

This will start all containers:
- PostgreSQL database
- Redis for caching
- Elasticsearch for search functionality
- RabbitMQ for messaging
- Spring Boot backend API
- Next.js frontend

### 4. Access the Application

- Frontend: http://localhost:3000
- Backend API: http://localhost:8082/api
- API Documentation: http://localhost:8082/api/swagger-ui.html

## Available Scripts

The project includes several npm scripts to help with Docker operations:

- `npm start` - Starts all Docker services (docker-compose up -d)
- `npm run stop` - Stops all Docker services (docker-compose down)
- `npm run restart` - Restarts all Docker services (docker-compose restart)
- `npm run logs` - Shows logs from all Docker services
- `npm run logs:backend` - Shows only backend logs
- `npm run logs:frontend` - Shows only frontend logs
- `npm run logs:db` - Shows only database logs
- `npm run build` - Builds all Docker images
- `npm run rebuild` - Rebuilds all Docker images from scratch (no cache)
- `npm run clean` - Stops all services and removes volumes
- `npm run status` - Shows the status of all Docker containers

## Docker Setup

### Available Services

The following services are configured in Docker Compose:
- PostgreSQL database (port 5432)
- Redis for caching (port 6379)
- Elasticsearch for search functionality (port 9200)
- RabbitMQ for messaging (ports 5672, 15672)
- Spring Boot backend API (port 8082)
- Next.js frontend (port 3000)

### Backend Dockerization

The Spring Boot backend has been dockerized. See `backend/README.docker.md` for detailed information.

Features include:
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

## Development

### Modifying the Frontend

Make changes to the code in the `frontend/` directory. The Docker container will automatically detect changes.

### Modifying the Backend

Make changes to the code in the `backend/` directory, then rebuild and restart the backend service:

```bash
npm run build # Builds all Docker images
npm run restart # Restarts all services
```

For more targeted approach:
```bash
docker-compose build backend
docker-compose restart backend
```

## Troubleshooting

### Container Issues

If containers aren't starting properly:

```bash
# Check container status
npm run status

# View detailed logs
npm run logs
```

### Database Connection Issues

If the backend can't connect to PostgreSQL:

1. Check if PostgreSQL container is running:
   ```bash
   npm run status
   ```

2. Check PostgreSQL logs:
   ```bash
   npm run logs:db
   ```

### Container Cleanup

If you need a fresh start:

```bash
# Stop all containers and remove volumes
npm run clean

# Rebuild from scratch
npm run rebuild

# Start services again
npm start
``` 