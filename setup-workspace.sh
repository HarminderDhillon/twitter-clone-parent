#!/bin/bash

# Colors for terminal output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Setting up Twitter Clone monorepo workspace...${NC}"

# Check Node.js version
NODE_VERSION=$(node -v)
echo -e "${YELLOW}Detected Node.js version: ${NODE_VERSION}${NC}"
echo -e "${YELLOW}Required Node.js version: >=18.0.0${NC}"

# Install root level dependencies
echo -e "${GREEN}Installing root level dependencies...${NC}"
npm install

# Install frontend dependencies using workspaces
echo -e "${GREEN}Installing frontend dependencies...${NC}"
npm install --workspace=frontend

# Check if Maven is installed
if command -v mvn &> /dev/null; then
    echo -e "${GREEN}Maven found. Installing backend dependencies...${NC}"
    cd backend && ./mvnw dependency:go-offline -B && cd ..
else
    echo -e "${YELLOW}Maven not found. Backend dependencies will be installed when you run the application.${NC}"
fi

# Check if Docker is available
if command -v docker &> /dev/null && command -v docker-compose &> /dev/null; then
    echo -e "${GREEN}Docker found.${NC}"
    echo -e "${YELLOW}Do you want to start the Docker services (PostgreSQL, Redis, etc.)? (y/n)${NC}"
    read -p "" start_docker
    if [[ "$start_docker" =~ ^[Yy]$ ]]; then
        echo -e "${GREEN}Starting Docker services...${NC}"
        docker-compose up -d postgres redis
    fi
else
    echo -e "${YELLOW}Docker not found. You will need to have PostgreSQL running locally or in another way.${NC}"
fi

echo -e "${GREEN}Workspace setup complete!${NC}"
echo -e "${YELLOW}To start the application, run:${NC}"
echo -e "npm run start"
echo -e "${YELLOW}To run in Docker, run:${NC}"
echo -e "npm run docker" 