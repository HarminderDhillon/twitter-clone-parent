#!/bin/bash

# Colors for terminal output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Cleaning up frontend files from twitter-clone backend project...${NC}"

# Navigate to the backend directory
cd twitter-clone

# List of frontend-related directories to remove
DIRECTORIES_TO_REMOVE=(
  "app"
  "components"
  "node_modules"
  ".next"
  "services"
  "lib"
  "types"
)

# List of frontend-related files to remove
FILES_TO_REMOVE=(
  "next.config.js"
  "package.json"
  "package-lock.json"
  "next-env.d.ts"
  "postcss.config.js"
  "tailwind.config.js"
  "tsconfig.json"
  "setup-ui.sh"
  ".env.local"
)

# Remove directories
echo -e "${YELLOW}Removing frontend directories...${NC}"
for dir in "${DIRECTORIES_TO_REMOVE[@]}"; do
  if [ -d "$dir" ]; then
    echo "Removing $dir/"
    rm -rf "$dir"
  fi
done

# Remove files
echo -e "${YELLOW}Removing frontend files...${NC}"
for file in "${FILES_TO_REMOVE[@]}"; do
  if [ -f "$file" ]; then
    echo "Removing $file"
    rm -f "$file"
  fi
done

# Update the .gitignore file to remove frontend-related patterns
echo -e "${YELLOW}Updating .gitignore file...${NC}"
if [ -f ".gitignore" ]; then
  # Create a temporary file with backend-specific patterns
  cat > .gitignore.new << EOF
# Backend-specific .gitignore

# Maven
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

# IDE files
.idea/
*.iws
*.iml
*.ipr
.vscode/
.classpath
.project
.settings/
.factorypath

# Compiled class files
*.class

# Log files
*.log
logs/

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# JVM crash logs
hs_err_pid*
replay_pid*

# Spring Boot
.spring.boot.properties
application-*.yml
!application-dev.yml
!application-test.yml
!application-prod.yml

# OS-specific files
.DS_Store
Thumbs.db
EOF

  # Replace the old .gitignore with the new one
  mv .gitignore.new .gitignore
  echo "Updated .gitignore file"
fi

echo -e "${GREEN}Cleanup complete!${NC}"
echo -e "${YELLOW}You should now commit these changes to the backend repository:${NC}"
echo -e "cd twitter-clone"
echo -e "git add -A"
echo -e "git commit -m \"Remove frontend files to make this a pure backend project\""
echo -e "git push" 