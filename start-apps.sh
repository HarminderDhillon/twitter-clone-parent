#!/bin/bash

# Colors for terminal output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting Twitter Clone Application...${NC}"

# Check if Docker is installed
if command -v docker &> /dev/null && command -v docker-compose &> /dev/null; then
    # Check if the Docker Compose services are running
    if docker ps | grep -q twitter-clone-postgres; then
        echo -e "${GREEN}PostgreSQL Docker container is already running.${NC}"
    else
        echo -e "${YELLOW}Required Docker services are not running.${NC}"
        read -p "Do you want to start PostgreSQL and Redis using Docker? (y/n): " start_docker
        if [[ "$start_docker" =~ ^[Yy]$ ]]; then
            echo -e "${GREEN}Starting Docker services...${NC}"
            docker-compose up -d postgres redis
            echo -e "${YELLOW}Waiting for PostgreSQL to be ready...${NC}"
            sleep 5  # Give PostgreSQL a moment to start up
        fi
    fi
else
    # Check if PostgreSQL is running
    echo -e "${YELLOW}Checking PostgreSQL connection...${NC}"
    if command -v pg_isready &> /dev/null; then
        if pg_isready -h localhost -p 5432 -q; then
            echo -e "${GREEN}PostgreSQL is running.${NC}"
        else
            echo -e "${RED}PostgreSQL is not running on localhost:5432.${NC}"
            echo -e "${YELLOW}Please start PostgreSQL using one of these commands:${NC}"
            echo -e "  - brew services start postgresql (macOS with Homebrew)"
            echo -e "  - sudo service postgresql start (Ubuntu/Debian)"
            echo -e "  - docker run -d --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=twitterclone -p 5432:5432 postgres:14"
            echo -e "${YELLOW}After starting PostgreSQL, run this script again.${NC}"
            read -p "Do you want to continue anyway? (y/n): " continue_anyway
            if [[ ! "$continue_anyway" =~ ^[Yy]$ ]]; then
                exit 1
            fi
        fi
    else
        echo -e "${YELLOW}pg_isready command not found. Cannot check PostgreSQL status.${NC}"
        echo -e "${YELLOW}Docker is not installed or docker-compose command not found.${NC}"
        echo -e "${YELLOW}Please ensure PostgreSQL is running at localhost:5432 with:${NC}"
        echo -e "  - Database name: twitterclone"
        echo -e "  - Username: postgres"
        echo -e "  - Password: postgres"
        read -p "Do you want to continue? (y/n): " continue_anyway
        if [[ ! "$continue_anyway" =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
fi

# Check if tmux is installed
if ! command -v tmux &> /dev/null; then
    echo -e "${YELLOW}tmux is not installed. Starting applications in separate terminals.${NC}"
    
    # Start backend
    echo -e "${GREEN}Starting Spring Boot backend...${NC}"
    cd twitter-clone
    ./mvnw spring-boot:run &
    
    # Start frontend
    echo -e "${GREEN}Starting Next.js frontend...${NC}"
    cd ../twitter-clone-ui
    npm install
    npm run dev &
    
    echo -e "${GREEN}Applications started. Backend: http://localhost:8081/api, Frontend: http://localhost:3000${NC}"
    exit 0
fi

# Create a new tmux session
SESSION_NAME="twitter-clone"
tmux new-session -d -s $SESSION_NAME

# Split the window horizontally
tmux split-window -h -t $SESSION_NAME

# Start backend in the left pane
tmux send-keys -t $SESSION_NAME:0.0 "cd twitter-clone && echo -e '${GREEN}Starting Spring Boot backend...${NC}' && ./mvnw spring-boot:run" C-m

# Start frontend in the right pane
tmux send-keys -t $SESSION_NAME:0.1 "cd twitter-clone-ui && echo -e '${GREEN}Starting Next.js frontend...${NC}' && npm install && npm run dev" C-m

# Attach to the session
echo -e "${GREEN}Applications starting in tmux session. Backend: http://localhost:8081/api, Frontend: http://localhost:3000${NC}"
echo -e "${YELLOW}Press Ctrl+B then D to detach from tmux session without stopping the applications.${NC}"
tmux attach-session -t $SESSION_NAME 