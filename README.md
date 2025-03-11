# Twitter Clone Application

This is a full-stack Twitter clone application with a Spring Boot backend and Next.js frontend.

## Project Structure

- `backend/` - Spring Boot backend (REST API)
- `frontend/` - Next.js frontend
- `nginx/` - Nginx reverse proxy configuration

## Architecture

The application uses a microservices architecture with the following components:

1. **Frontend**: A Next.js application that provides the user interface
2. **Backend**: A Spring Boot application that provides the REST API
3. **Nginx**: A reverse proxy that routes requests between the frontend and backend
4. **PostgreSQL**: A database for persistent storage
5. **Redis**: A cache for temporary data storage
6. **Elasticsearch**: A search engine for efficient data retrieval
7. **RabbitMQ**: A message broker for asynchronous communication

### API Routing

The application uses Nginx as a reverse proxy to route API requests:

- All requests to `/api/*` are routed to the backend (Spring Boot) service
- Requests to Next.js API routes (like `/api/login` and `/api/register`) are routed to the frontend service
- The frontend uses these API routes to communicate with the backend securely

This architecture provides several benefits:
- Simplified URL management (no need to specify ports)
- Improved security through a single entry point
- Better scalability for future enhancements
- Enhanced development experience

### Authentication Architecture

The application implements a modern authentication system with these components:

1. **Frontend Authentication Flow**:
   - User signup and login forms in the Next.js frontend
   - Next.js API routes (`/api/register` and `/api/login`) that act as proxies
   - These routes forward requests to the appropriate backend endpoints
   - Error handling and response processing at the frontend level

2. **Backend Authentication Services**:
   - Spring Security with JWT token authentication
   - Dedicated AuthController for handling login requests
   - UserController for user registration and management
   - Password encryption and secure token generation

3. **Nginx Integration**:
   - Routes authentication requests to the appropriate service
   - Ensures Next.js API routes are handled by the frontend
   - Routes other API requests to the backend

This multi-layer architecture provides secure, reliable user authentication while maintaining a clean separation of concerns between frontend and backend components.

## Prerequisites

- Docker and Docker Compose
- npm (for convenience commands - optional)

No need to install Java, Maven, Node.js, PostgreSQL or other dependencies locally - everything runs in Docker!

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/HarminderDhillon/twitter-clone-parent.git
cd twitter-clone-parent
```

### 2. Setup and Start the Application

The application can be set up and run with simple Docker Compose commands:

#### Using npm (Recommended for convenience)

```bash
# Install minimal npm dependencies for convenience scripts
npm install

# Build and start all services
npm run setup
npm start
```

#### Using Docker Compose directly

```bash
# Build all Docker images
docker-compose build

# Start all services
docker-compose up -d

# Display application information
docker-compose run --rm app-info
```

### 3. Access the Application

After starting the application, the access URLs will be automatically displayed on your screen.

You can view this information again anytime with:
```bash
npm run info
```
or
```bash
docker-compose run --rm status
```

Standard URLs (if port 3000 is available):
- Frontend: http://localhost:3000
- Backend API: http://localhost:8082/api
- API Documentation: http://localhost:8082/api/swagger-ui.html

**Note**: If port 3000 is already in use, Docker will automatically use the next available port in the range 3000-3010. The actual port will be displayed when you run `npm start` or `npm run info`.

## Stopping and Restarting the Application

### Stopping the Application

To stop all running containers without removing data:

```bash
npm run stop
```
or
```bash
docker-compose down
```

This safely shuts down all Docker containers while preserving your data in volumes.

### Restarting the Application

To restart all services (for example, after making code changes):

```bash
npm run restart
```
or
```bash
docker-compose restart
docker-compose run --rm app-info
```

This will restart all containers and display the current access URLs when services are ready.

### Complete Cleanup and Restart

If you need a fresh start (this will delete all data):

```bash
npm run clean    # Stops containers and removes all volumes (deletes all data)
npm start        # Starts everything again from scratch
```
or
```bash
docker-compose down -v    # Stops containers and removes all volumes (deletes all data)
docker-compose up -d      # Starts everything again from scratch
```

### Targeted Restarts

For development, you may want to restart only specific services:

```bash
# Restart only the backend
docker-compose restart backend

# Restart only the frontend
docker-compose restart frontend

# Check the current URLs
npm run info
```

## Port Detection and Availability

The project includes robust port conflict handling:

1. The frontend container can use any port in the range 3000-3010
2. The status service will automatically detect which port is being used
3. If port 3000 is in use by another application, Docker will use the next available port
4. Run `npm run info` at any time to see the current port assignments

## Available Scripts

The project includes several npm scripts to help with Docker operations:

- `npm start` - Starts all Docker services and displays access URLs
- `npm run stop` - Stops all Docker services
- `npm run restart` - Restarts all Docker services and displays access URLs
- `npm run info` - Displays access URLs for running services (works even if ports change)
- `npm run logs` - Shows logs from all Docker services
- `npm run logs:backend` - Shows only backend logs
- `npm run logs:frontend` - Shows only frontend logs
- `npm run logs:db` - Shows only database logs
- `npm run build` - Builds all Docker images
- `npm run rebuild` - Rebuilds all Docker images from scratch (no cache)
- `npm run clean` - Stops all services and removes volumes
- `npm run status` - Shows the status of all Docker containers
- `npm run fix-nextjs` - Fixes Next.js installation if there are issues
- `npm run setup` - Sets up the workspace by building all Docker images

## Docker Setup

### Available Services

The following services are configured in Docker Compose:
- PostgreSQL database (port 5432)
- Redis for caching (port 6379)
- Elasticsearch for search functionality (port 9200)
- RabbitMQ for messaging (ports 5672, 15672)
- Spring Boot backend API (port 8082)
- Next.js frontend (port 3000-3010, automatically selected)
- Status service (displays URL information)
- App Info service (one-time display of URL information after startup)
- Fix Next.js service (used to fix Next.js issues if they arise)

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
- Flexible port mapping with conflict resolution

## Development

### Modifying the Frontend

Make changes to the code in the `frontend/` directory. The Docker container will automatically detect changes.

### Modifying the Backend

Make changes to the code in the `backend/` directory, then rebuild and restart the backend service:

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
# or
docker-compose ps

# View detailed logs
npm run logs
# or
docker-compose logs -f
```

### Port Conflicts

If you're seeing issues with port assignments:

```bash
# Check which ports are being used
npm run info
# or
docker-compose run --rm status

# If the frontend port isn't what you expect, try restarting
npm run restart
# or
docker-compose restart
```

### Database Connection Issues

If the backend can't connect to PostgreSQL:

1. Check if PostgreSQL container is running:
   ```bash
   npm run status
   # or
   docker-compose ps
   ```

2. Check PostgreSQL logs:
   ```bash
   npm run logs:db
   # or
   docker-compose logs -f postgres
   ```

### Next.js Issues

If you encounter issues with the Next.js frontend:

```bash
# Fix Next.js installation
npm run fix-nextjs

# Restart the frontend
docker-compose restart frontend
```

### Container Cleanup

If you need a fresh start:

```bash
# Stop all containers and remove volumes
npm run clean
# or
docker-compose down -v

# Rebuild from scratch
npm run rebuild
# or
docker-compose build --no-cache

# Start services again
npm start
# or
docker-compose up -d
``` 