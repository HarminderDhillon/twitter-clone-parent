# Dockerizing the Twitter Clone Frontend

This document describes how the Next.js frontend for the Twitter Clone application has been dockerized.

## Docker Setup

The frontend application uses a multi-stage Docker build for optimized image size and build performance.

### Dockerfile Overview

The Dockerfile consists of two stages:

1. **Build Stage**: Compiles and builds the Next.js application
2. **Runtime Stage**: Contains only the necessary files to run the production build

```dockerfile
# Build stage
FROM node:18-alpine AS builder
WORKDIR /app

# Copy package files and install dependencies
COPY package.json package-lock.json ./
RUN npm ci

# Copy application code
COPY . .

# Set the correct API URL for production
RUN echo "NEXT_PUBLIC_API_URL=http://backend:8081/api" > .env.production

# Build the Next.js application
RUN npm run build

# Runtime stage
FROM node:18-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production

# Copy necessary files from build stage
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json
COPY --from=builder /app/.env.production ./.env.production

# Expose the port Next.js runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
```

### Key Features

- **Dependency Installation**: Installs dependencies using `npm ci` for faster, more reliable builds
- **Environment Configuration**: Sets the API URL environment variable for production
- **Multi-stage Build**: Reduces final image size by only including necessary files
- **Alpine Base Image**: Uses lightweight Node.js Alpine images for smaller footprint

## Running with Docker Compose

The frontend has been integrated into the docker-compose.yml file and will start automatically with:

```bash
docker-compose up -d
```

### Configuration in docker-compose.yml

```yaml
frontend:
  build:
    context: ./twitter-clone-ui
    dockerfile: Dockerfile
  container_name: twitter-clone-frontend
  ports:
    - "3000:3000"
  depends_on:
    - backend
  environment:
    - NEXT_PUBLIC_API_URL=http://backend:8081/api
  restart: unless-stopped
```

### Environment Variables

- `NEXT_PUBLIC_API_URL`: Points to the backend API service

## Development vs. Production

### Development Environment

For local development without Docker:

```bash
npm run dev
```

The application will use the settings in `.env.local` which points to `http://localhost:8080/api`.

### Production Environment

In the Docker production environment, the application uses `.env.production` which is created during the build process and points to `http://backend:8081/api`.

## Troubleshooting

### Common Issues

1. **API Connection Issues**: If the frontend can't connect to the API, check:
   - The backend container is running
   - The environment variables are set correctly
   - The network communication between containers is working

2. **Build Failures**: If the Docker build fails, try:
   - Running `npm ci` locally to verify dependencies can be installed
   - Checking for any specific Node.js version requirements
   - Freeing up disk space if the build process is running out of space

3. **Performance Issues**: If the application is slow:
   - Consider adding volume mounts for development to enable hot reloading
   - Add caching layers in the Dockerfile for better build performance 