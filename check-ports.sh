#!/bin/bash

# Colors for terminal output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}Checking active container ports...${NC}"

# Check if docker command is available
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed or not in PATH. Please install Docker to continue.${NC}"
    exit 1
fi

# Check if frontend container is running
if ! docker ps | grep -q twitter-clone-frontend; then
    echo -e "${YELLOW}Frontend container is not running. Starting containers...${NC}"
    docker-compose up -d
    
    # Wait for containers to start - increase timeout and add better feedback
    echo -e "${YELLOW}Waiting for containers to start and ports to be assigned...${NC}"
    MAX_ATTEMPTS=15
    ATTEMPT=0
    
    while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
        ATTEMPT=$((ATTEMPT+1))
        echo -n "."
        
        # Check if frontend container is running
        if docker ps | grep -q twitter-clone-frontend; then
            # Give it a moment for port assignment
            sleep 2
            echo -e "\n${GREEN}Containers started successfully!${NC}"
            break
        fi
        
        if [ $ATTEMPT -eq $MAX_ATTEMPTS ]; then
            echo -e "\n${RED}Failed to start containers after multiple attempts.${NC}"
            echo -e "${YELLOW}Check for errors with:${NC} docker-compose logs"
            exit 1
        fi
        
        sleep 2
    done
fi

echo -e "${BLUE}Detecting assigned ports...${NC}"

# Try several approaches to get the frontend port
FRONTEND_PORT=""

# First try the docker port command
if [ -z "$FRONTEND_PORT" ]; then
    FRONTEND_PORT=$(docker port twitter-clone-frontend 2>/dev/null | grep '0.0.0.0' | head -n 1 | awk '{print $3}' | cut -d':' -f2)
fi

# If that fails, try docker inspect
if [ -z "$FRONTEND_PORT" ]; then
    FRONTEND_PORT=$(docker inspect --format='{{range $p, $conf := .NetworkSettings.Ports}}{{with $conf}}{{if eq $p "3000/tcp"}}{{(index . 0).HostPort}}{{end}}{{end}}{{end}}' twitter-clone-frontend 2>/dev/null)
fi

# Last try with docker ps output parsing
if [ -z "$FRONTEND_PORT" ]; then
    FRONTEND_PORT=$(docker ps | grep twitter-clone-frontend | grep -oE '0.0.0.0:[0-9]+->3000/tcp' | cut -d: -f2 | cut -d- -f1)
fi

if [ -z "$FRONTEND_PORT" ]; then
    echo -e "${RED}Could not determine frontend port. Is the container running?${NC}"
    echo -e "${YELLOW}Container status:${NC}"
    docker ps | grep twitter-clone-frontend
    echo -e "${YELLOW}For detailed logs:${NC} npm run logs:frontend"
    exit 1
fi

# Get the backend port - same approach
BACKEND_PORT=""

# First try with docker port
if [ -z "$BACKEND_PORT" ]; then
    BACKEND_PORT=$(docker port twitter-clone-backend 2>/dev/null | grep '0.0.0.0' | head -n 1 | awk '{print $3}' | cut -d':' -f2)
fi

# If that fails, try docker inspect
if [ -z "$BACKEND_PORT" ]; then
    BACKEND_PORT=$(docker inspect --format='{{range $p, $conf := .NetworkSettings.Ports}}{{with $conf}}{{if eq $p "8081/tcp"}}{{(index . 0).HostPort}}{{end}}{{end}}{{end}}' twitter-clone-backend 2>/dev/null)
fi

# Last try with docker ps output parsing
if [ -z "$BACKEND_PORT" ]; then
    BACKEND_PORT=$(docker ps | grep twitter-clone-backend | grep -oE '0.0.0.0:[0-9]+->8081/tcp' | cut -d: -f2 | cut -d- -f1)
fi

if [ -z "$BACKEND_PORT" ]; then
    echo -e "${YELLOW}Could not determine backend port.${NC}"
else
    echo -e "${GREEN}Backend API is running at:${NC} http://localhost:${BACKEND_PORT}/api"
    echo -e "${GREEN}API Documentation:${NC} http://localhost:${BACKEND_PORT}/api/swagger-ui.html"
fi

# Display frontend port
echo -e "${GREEN}Frontend is running at:${NC} http://localhost:${FRONTEND_PORT}"

# Check if the port is the default one
if [ "$FRONTEND_PORT" != "3000" ]; then
    echo -e "${YELLOW}Note: Frontend is using a non-default port (${FRONTEND_PORT} instead of 3000)${NC}"
    echo -e "${YELLOW}This happens when port 3000 is already in use by another application.${NC}"
fi

# List all running services for reference
echo -e "\n${BLUE}All running services:${NC}"
docker ps --format "table {{.Names}}\t{{.Ports}}" | grep twitter-clone 