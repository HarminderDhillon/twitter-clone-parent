# Docker Setup for Twitter Clone Backend

This document describes how to build and run the Twitter Clone backend using Docker.

## Prerequisites

- Docker and Docker Compose installed on your system
- Java 21 if you want to develop locally

## Building and Running with Docker

### Option 1: Using Docker Compose (Recommended)

The easiest way to run the entire application stack is using Docker Compose:

```bash
# From the root directory
docker-compose up -d
```

This will:
1. Start PostgreSQL, Redis, Elasticsearch, and RabbitMQ services
2. Build and start the Spring Boot backend

To stop all services:

```bash
docker-compose down
```

To stop all services and remove volumes:

```bash
docker-compose down -v
```

### Option 2: Building Only the Backend Docker Image

If you want to build just the backend Docker image:

```bash
# From the twitter-clone directory
docker build -t twitter-clone-backend .
```

To run it standalone (note that this requires the dependent services to be available):

```bash
docker run -p 8082:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/twitterclone \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e SPRING_REDIS_HOST=host.docker.internal \
  -e SPRING_DATA_ELASTICSEARCH_CLUSTER-NODES=host.docker.internal:9200 \
  -e SPRING_RABBITMQ_HOST=host.docker.internal \
  twitter-clone-backend
```

## Environment Configuration

You can configure the application by setting environment variables:

| Environment Variable | Description | Default Value |
|---------------------|-------------|---------------|
| SPRING_PROFILES_ACTIVE | Active Spring profile | prod |
| SPRING_DATASOURCE_URL | PostgreSQL connection URL | jdbc:postgresql://postgres:5432/twitterclone |
| SPRING_DATASOURCE_USERNAME | PostgreSQL username | postgres |
| SPRING_DATASOURCE_PASSWORD | PostgreSQL password | postgres |
| SPRING_REDIS_HOST | Redis host | redis |
| SPRING_DATA_ELASTICSEARCH_CLUSTER-NODES | Elasticsearch URL | elasticsearch:9200 |
| SPRING_RABBITMQ_HOST | RabbitMQ host | rabbitmq |
| JWT_SECRET | Secret key for JWT tokens | (set a secure value) |

## Development Workflow

For development, you can:

1. Run dependency services using Docker Compose:
   ```bash
   docker-compose up -d postgres redis elasticsearch rabbitmq
   ```

2. Run the Spring Boot application locally:
   ```bash
   ./mvnw spring-boot:run
   ```

## Production Considerations

For production use:

1. Set a secure JWT_SECRET
2. Consider using an external image registry to store your Docker images
3. Set up proper monitoring and logging
4. Configure a proper database backup strategy
5. Set up a robust CI/CD pipeline for automated testing and deployment 