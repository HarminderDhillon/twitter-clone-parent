import { NextResponse } from 'next/server';

export async function POST(request: Request) {
  try {
    // Parse the request body
    const loginData = await request.json();
    
    console.log('Next.js API Route: Forwarding login request to backend');
    
    // Use absolute URL with Docker network name
    // The URL must be absolute when running on the server side
    const backendUrl = process.env.BACKEND_URL || 'http://backend:8081';
    console.log(`Using backend URL: ${backendUrl}/api/auth/login`);
    
    // Forward the request to the backend
    const response = await fetch(`${backendUrl}/api/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginData),
      // Important: This prevents Next.js from trying to reuse connections
      // which can cause issues in a server environment
      cache: 'no-store',
      next: { revalidate: 0 }
    });
    
    if (!response.ok) {
      console.error(`Backend returned error status: ${response.status}`);
      const errorText = await response.text();
      console.error(`Error response: ${errorText}`);
      return NextResponse.json(
        { message: `Backend error: ${response.statusText}`, details: errorText },
        { status: response.status }
      );
    }
    
    // Get the response data
    const responseData = await response.json();
    
    console.log('Login response status:', response.status);
    console.log('Login response data:', JSON.stringify(responseData).substring(0, 200));
    
    // Return the response with the appropriate status
    return NextResponse.json(responseData);
  } catch (error) {
    console.error('Error in login API route:', error);
    return NextResponse.json(
      { message: `Internal server error: ${error instanceof Error ? error.message : String(error)}` },
      { status: 500 }
    );
  }
} 