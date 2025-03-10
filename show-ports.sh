#!/bin/sh

# Find which port in range 3000-3010 is being used by the frontend
PORT_CHECK=$(netstat -tln 2>/dev/null | grep -E ':300[0-9]' || lsof -i:3000-3010 2>/dev/null | grep -i listen || ss -tln | grep -E ':300[0-9]')
FRONTEND_PORT=$(echo "$PORT_CHECK" | grep -o -E '300[0-9]' | head -n1)

if [ -z "$FRONTEND_PORT" ]; then
  # Try Docker method as backup if network check failed
  DOCKER_UP=$(docker ps 2>/dev/null | grep twitter-clone-frontend)
  if [ -n "$DOCKER_UP" ]; then
    FRONTEND_STATUS="http://localhost:3000"
  else
    FRONTEND_STATUS="not running"
  fi
else
  FRONTEND_STATUS="http://localhost:$FRONTEND_PORT"
fi

# Check backend
BACKEND_PORT=$(netstat -tln 2>/dev/null | grep -E ':8082' || lsof -i:8082 2>/dev/null | grep -i listen || ss -tln | grep -E ':8082')
if [ -z "$BACKEND_PORT" ]; then
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
if [[ "$FRONTEND_STATUS" == "not running" ]]; then
  echo "To start all services: npm start"
fi 