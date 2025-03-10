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

## Learn More

To learn more about the technologies used in this project:

- [Next.js Documentation](https://nextjs.org/docs)
- [React Documentation](https://react.dev/)
- [Tailwind CSS](https://tailwindcss.com/docs) 