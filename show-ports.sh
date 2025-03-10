#!/bin/sh

# Colors for terminal output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get frontend port
FRONTEND_PORT=$(docker ps -f name=twitter-clone-frontend --format '{{.Ports}}' | grep -o '0.0.0.0:[0-9]*->3000' | cut -d ':' -f2 | cut -d '-' -f1)
if [ -z "$FRONTEND_PORT" ]; then
  FRONTEND_STATUS="${GREEN}not running${NC}"
else
  FRONTEND_STATUS="${GREEN}http://localhost:$FRONTEND_PORT${NC}"
fi

# Check backend
BACKEND_RUNNING=$(docker ps -f name=twitter-clone-backend --format '{{.Names}}')
if [ -z "$BACKEND_RUNNING" ]; then
  BACKEND_STATUS="${GREEN}not running${NC}"
  API_DOCS_STATUS="${GREEN}not running${NC}"
else
  BACKEND_STATUS="${GREEN}http://localhost:8082/api${NC}"
  API_DOCS_STATUS="${GREEN}http://localhost:8082/api/swagger-ui.html${NC}"
fi

# Display all info
echo "${BLUE}===== Twitter Clone Application Status =====${NC}"
echo "Frontend:         $FRONTEND_STATUS"
echo "Backend API:      $BACKEND_STATUS"
echo "API Documentation: $API_DOCS_STATUS"
echo "${BLUE}==============================================${NC}"

# If frontend is not running, provide instructions
if [ -z "$FRONTEND_PORT" ]; then
  echo "To start all services: npm start"
fi 