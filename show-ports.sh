#!/bin/sh

# Find which port in range 3000-3010 is being used by the frontend
FRONTEND_PORT=3000
FRONTEND_STATUS="not running"

# Try to check each port directly with curl
for port in $(seq 3000 3010); do
  if curl -s -m 1 -o /dev/null -w "" http://localhost:$port 2>/dev/null; then
    FRONTEND_PORT=$port
    FRONTEND_STATUS="http://localhost:$port"
    break
  fi
done

# If curl check failed, use various platform-specific methods
if [ "$FRONTEND_STATUS" = "not running" ]; then
  # Try using the docker command
  if command -v docker >/dev/null; then
    DOCKER_UP=$(docker ps 2>/dev/null | grep twitter-clone-frontend)
    if [ -n "$DOCKER_UP" ]; then
      # Try to get the port from docker port output
      PORT_NUM=$(docker port twitter-clone-frontend 2>/dev/null | grep -o '0.0.0.0:[0-9]*->3000' | head -n1 | cut -d':' -f2 | cut -d'-' -f1)
      if [ -n "$PORT_NUM" ]; then
        FRONTEND_STATUS="http://localhost:$PORT_NUM"
      else
        FRONTEND_STATUS="http://localhost:3000"
      fi
    fi
  fi
fi

# Check backend
BACKEND_STATUS="not running"
API_DOCS_STATUS="not running"

# Check if backend port is accessible
if curl -s -m 1 -o /dev/null -w "" http://localhost:8082/api 2>/dev/null; then
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
if [ "$FRONTEND_STATUS" = "not running" ]; then
  echo "To start all services: npm start"
fi 