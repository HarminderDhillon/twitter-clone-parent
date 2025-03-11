#!/bin/bash

echo "Testing user registration API..."

curl -X POST http://localhost:80/api/users \
     -H "Content-Type: application/json" \
     -d '{
         "username": "testuser12345",
         "email": "testuser12345@example.com",
         "password": "password123",
         "displayName": "Test User"
     }' \
     -v 