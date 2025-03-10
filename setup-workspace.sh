#!/bin/bash

# Colors for terminal output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Setting up Twitter Clone monorepo workspace...${NC}"

# Check Docker is installed
if ! command -v docker &> /dev/null || ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker and/or docker-compose not found.${NC}"
    echo -e "${YELLOW}Please install Docker Desktop or Docker Engine with Compose to continue.${NC}"
    exit 1
fi

# Install root level npm dependencies (minimal, just for scripts)
echo -e "${GREEN}Installing minimal npm dependencies for scripts...${NC}"
npm install --no-package-lock

echo -e "${GREEN}Docker found. Building Docker images...${NC}"
read -p "Do you want to build the Docker images now? This may take a few minutes. (y/n): " build_images
if [[ "$build_images" =~ ^[Yy]$ ]]; then
    docker-compose build
    echo -e "${GREEN}Docker images built successfully.${NC}"
else
    echo -e "${YELLOW}Skipping image build. You can build them later with 'npm run build'.${NC}"
fi

echo -e "${GREEN}Workspace setup complete!${NC}"
echo -e "${YELLOW}To start the application, run:${NC}"
echo -e "npm start"
echo -e "${YELLOW}To view logs, run:${NC}"
echo -e "npm run logs"
echo -e "${YELLOW}To check the status of containers, run:${NC}"
echo -e "npm run status"
echo -e "${YELLOW}To stop the application, run:${NC}"
echo -e "npm run stop" 