# Build stage
FROM node:18-alpine AS builder
WORKDIR /app

# Copy package files and install dependencies
COPY package.json package-lock.json ./
RUN npm ci

# Copy application code
COPY . .

# Set the correct API URL for production - using internal Docker network name
RUN echo "NEXT_PUBLIC_API_URL=/api" > .env.production
RUN echo "BACKEND_URL=http://backend:8081" >> .env.production

# Build the Next.js application
RUN npm run build

# Runtime stage
FROM node:18-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production
# Set environment variables for runtime
ENV NEXT_PUBLIC_API_URL=/api
ENV BACKEND_URL=http://backend:8081

# Copy necessary files from build stage
COPY --from=builder /app/next.config.js ./
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json
COPY --from=builder /app/.env.production ./.env.production

# Expose the port Next.js runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"] 