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
    npm start
    # Wait a moment for containers to start
    sleep 5
fi

# Get the frontend container port mapping
FRONTEND_PORT=$(docker port twitter-clone-frontend 2>/dev/null | grep '0.0.0.0' | head -n 1 | awk '{print $3}' | cut -d':' -f2)

if [ -z "$FRONTEND_PORT" ]; then
    echo -e "${RED}Could not determine frontend port. Is the container running?${NC}"
    echo -e "${YELLOW}Check container status with:${NC} npm run status"
    exit 1
fi

# Get the backend port
BACKEND_PORT=$(docker port twitter-clone-backend 2>/dev/null | grep '0.0.0.0' | head -n 1 | awk '{print $3}' | cut -d':' -f2)

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