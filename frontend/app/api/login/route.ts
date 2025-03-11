import { NextResponse } from 'next/server';

export async function POST(request: Request) {
  try {
    // Parse the request body
    const loginData = await request.json();
    
    console.log('Next.js API Route: Forwarding login request to backend');
    
    // Forward the request to the backend
    const backendUrl = process.env.BACKEND_URL || 'http://backend:8081';
    const response = await fetch(`${backendUrl}/api/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginData),
    });
    
    // Get the response data
    const responseData = await response.json();
    
    // Return the response with the appropriate status
    return NextResponse.json(
      responseData,
      { status: response.status }
    );
  } catch (error) {
    console.error('Error in login API route:', error);
    return NextResponse.json(
      { message: 'Internal server error' },
      { status: 500 }
    );
  }
} 