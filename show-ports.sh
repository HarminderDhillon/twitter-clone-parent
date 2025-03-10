#!/bin/sh

# Get frontend port
FRONTEND_PORT=$(docker ps -f name=twitter-clone-frontend --format '{{.Ports}}' | grep -o '0.0.0.0:[0-9]*->3000' | cut -d ':' -f2 | cut -d '-' -f1)
if [ -z "$FRONTEND_PORT" ]; then
  FRONTEND_STATUS="not running"
else
  FRONTEND_STATUS="http://localhost:$FRONTEND_PORT"
fi

# Check backend
BACKEND_RUNNING=$(docker ps -f name=twitter-clone-backend --format '{{.Names}}')
if [ -z "$BACKEND_RUNNING" ]; then
  BACKEND_STATUS="not running"
  API_DOCS_STATUS="not running"
else
  BACKEND_STATUS="http://localhost:8082/api"
  API_DOCS_STATUS="http://localhost:8082/api/swagger-ui.html"
fi

# Display all info
echo "===== Twitter Clone Application Status ====="
echo "Frontend:         $FRONTEND_STATUS"
echo "Backend API:      $BACKEND_STATUS"
echo "API Documentation: $API_DOCS_STATUS"
echo "============================================"

# If frontend is not running, provide instructions
if [ -z "$FRONTEND_PORT" ]; then
  echo "To start all services: npm start"
fi 