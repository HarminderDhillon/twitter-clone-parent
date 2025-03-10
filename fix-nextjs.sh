#!/bin/bash

# Colors for terminal output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Fixing Next.js installation...${NC}"

# Navigate to the frontend directory
cd frontend

# Clean the installation
echo -e "${YELLOW}Cleaning node_modules and .next directories...${NC}"
rm -rf node_modules
rm -rf .next
rm -f package-lock.json

# Update Next.js to a stable version (14.1.0 instead of 14.2.0 which has vulnerabilities)
echo -e "${YELLOW}Updating Next.js to a stable version...${NC}"
sed -i '' 's/"next": "14.2.0"/"next": "14.1.0"/g' package.json
sed -i '' 's/"eslint-config-next": "14.2.0"/"eslint-config-next": "14.1.0"/g' package.json

# Reinstall dependencies
echo -e "${YELLOW}Reinstalling dependencies...${NC}"
npm install

echo -e "${GREEN}Next.js installation fixed. Please try running the application again.${NC}"
echo -e "${YELLOW}You can use the following command to start the frontend:${NC}"
echo -e "cd frontend && npm run dev" 