# Twitter Clone Frontend

This is the frontend application for the Twitter Clone project, built with Next.js, React, and Tailwind CSS.

## Getting Started

### Prerequisites

- Node.js 18 or later
- npm or yarn

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd twitter-clone-ui
```

2. Install dependencies:
```bash
npm install
# or
yarn install
```

3. Create a `.env.local` file in the root directory and add:
```
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

### Development

Run the development server:

```bash
npm run dev
# or
yarn dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

### Production Build

Build the application for production:

```bash
npm run build
# or
yarn build
```

Start the production server:

```bash
npm run start
# or
yarn start
```

## Project Structure

- `/app`: Contains the Next.js 13+ App Router pages and layouts
- `/components`: Reusable UI components
- `/services`: API services and utilities for data fetching
- `/lib`: Utility functions and shared code
- `/types`: TypeScript type definitions

## Features

- User authentication (login/register)
- View and create tweets (posts)
- Follow/unfollow other users
- Like and retweet posts
- User profile management
- Timeline feed of followed users' posts
- Responsive design with Tailwind CSS

## Backend Integration

This frontend application communicates with the Spring Boot backend API running at `http://localhost:8080/api`. 
API requests are proxied through Next.js to avoid CORS issues in development.

## API Routing

The application uses a combination of Next.js API routes and direct backend calls:

- **Next.js API Routes**: Used for authentication (/api/login, /api/register) and other frontend-specific functionality. These routes are handled by the Next.js server running on the frontend.
- **Backend API Calls**: Direct calls to the backend for data operations like fetching posts, user profiles, etc.

When running in Docker with Nginx as a reverse proxy, the routing is managed as follows:

1. Requests to `/api/login` and `/api/register` are routed to the Next.js server
2. All other `/api/` requests are forwarded to the backend Spring Boot application

### Authentication Flow Architecture

The registration and login processes follow this architecture:

1. **User Signup Process**:
   - User submits the signup form on the frontend
   - The form submission is sent to the Next.js API route `/api/register`
   - The Next.js API route acts as a proxy and forwards the request to the backend's `/api/users` endpoint
   - The backend processes the registration request and returns a response
   - The Next.js API route handles any errors and returns an appropriate response to the frontend
   - The frontend displays success or error messages to the user

2. **User Login Process**:
   - User submits the login form on the frontend
   - The form submission is sent to the Next.js API route `/api/login`
   - The Next.js API route forwards the request to the backend's `/api/auth/login` endpoint
   - Upon successful authentication, the backend returns a JWT token
   - The Next.js API route stores the token and returns a success response
   - The frontend redirects the user to their dashboard

This architecture provides several benefits:
- **Avoids CORS issues**: All API requests from the browser are to the same domain
- **Enhanced error handling**: The Next.js API routes can provide more user-friendly error messages
- **Simplified frontend code**: The frontend components don't need to know about backend URLs or authentication details
- **Improved security**: Authentication tokens can be managed server-side in the Next.js API routes

## Development Tools

The application includes several testing and debugging tools:

- **API Test Page**: Available at `/test-api`, this page allows testing the API routes directly from the browser
- **Debug Mode**: Set `DEBUG=true` in your `.env.local` file to enable additional logging

## Learn More

To learn more about the technologies used in this project:

- [Next.js Documentation](https://nextjs.org/docs)
- [React Documentation](https://react.dev/)
- [Tailwind CSS](https://tailwindcss.com/docs) 