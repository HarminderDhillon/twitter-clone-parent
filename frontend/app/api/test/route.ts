import { NextResponse } from 'next/server';

export async function GET() {
  // This is a simple API route that just returns a success message
  // We'll use this to verify that Next.js API routes are working
  
  console.log('Test API route called');
  
  return NextResponse.json({
    status: 'success',
    message: 'Next.js API route is working correctly',
    timestamp: new Date().toISOString()
  });
}

export async function POST(request: Request) {
  try {
    // Get the request body if any
    const body = await request.text();
    let parsedBody = {};
    
    try {
      if (body) {
        parsedBody = JSON.parse(body);
      }
    } catch (e) {
      console.error('Failed to parse request body:', e);
    }
    
    console.log('Test POST API route called with body:', parsedBody);
    
    return NextResponse.json({
      status: 'success',
      message: 'Next.js POST API route is working correctly',
      receivedData: parsedBody,
      timestamp: new Date().toISOString()
    });
  } catch (error) {
    console.error('Error in test API route:', error);
    return NextResponse.json(
      { 
        status: 'error',
        message: 'An error occurred in the test API route',
        error: error instanceof Error ? error.message : String(error)
      },
      { status: 500 }
    );
  }
} 