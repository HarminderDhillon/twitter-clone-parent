import { NextResponse } from 'next/server';

export async function GET() {
  try {
    // Check backend health
    const backendUrl = process.env.BACKEND_URL || 'http://backend:8081';
    
    try {
      const backendResponse = await fetch(`${backendUrl}/api/users`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }).then(res => res.ok ? 'healthy' : `status: ${res.status}`);
      
      return NextResponse.json({
        status: 'ok',
        timestamp: new Date().toISOString(),
        services: {
          frontend: 'ok',
          backend: backendResponse
        }
      });
    } catch (error) {
      return NextResponse.json({
        status: 'ok',
        timestamp: new Date().toISOString(),
        services: {
          frontend: 'ok',
          backend: 'unavailable'
        }
      });
    }
  } catch (error) {
    console.error('Error in health API route:', error);
    return NextResponse.json(
      { status: 'error', message: 'Internal server error' },
      { status: 500 }
    );
  }
} 