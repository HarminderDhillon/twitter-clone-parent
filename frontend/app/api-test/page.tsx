'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function ApiTestPage() {
  const router = useRouter();
  const [result, setResult] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [username, setUsername] = useState<string>(`testuser${Date.now()}`);
  const [email, setEmail] = useState<string>(`testuser${Date.now()}@example.com`);

  const testRegistration = async () => {
    setLoading(true);
    setResult('');
    try {
      // Generate unique username/email to avoid conflicts
      const uniqueId = Date.now();
      const testUsername = `testuser${uniqueId}`;
      const testEmail = `testuser${uniqueId}@example.com`;
      
      // Update the state for the form fields
      setUsername(testUsername);
      setEmail(testEmail);
      
      // Test direct fetch
      const response = await fetch('/api/users', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: testUsername,
          email: testEmail,
          password: 'password123',
          displayName: 'Test User',
        }),
      });
      
      // Get the response data regardless of status
      const responseText = await response.text();
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (e) {
        data = { raw: responseText };
      }
      
      // Format the result with status information
      setResult(
        `Status: ${response.status} ${response.statusText}\n\n` +
        `Response:\n${JSON.stringify(data, null, 2)}`
      );
    } catch (error) {
      console.error('API test error:', error);
      setResult(`Error: ${error instanceof Error ? error.message : String(error)}`);
    } finally {
      setLoading(false);
    }
  };

  const testBackendHealth = async () => {
    setLoading(true);
    setResult('');
    try {
      const response = await fetch('/api/', {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      });
      
      // Get the response data regardless of status
      const responseText = await response.text();
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (e) {
        data = { raw: responseText };
      }
      
      // Format the result with status information
      setResult(
        `Status: ${response.status} ${response.statusText}\n\n` +
        `Response:\n${JSON.stringify(data, null, 2)}`
      );
    } catch (error) {
      console.error('API health check error:', error);
      setResult(`Error: ${error instanceof Error ? error.message : String(error)}`);
    } finally {
      setLoading(false);
    }
  };

  const testBackendDirect = async () => {
    setLoading(true);
    setResult('');
    try {
      // Test the backend directly using the full URL
      const response = await fetch('http://localhost:8082/api/users', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: `testuser${Date.now()}`,
          email: `testuser${Date.now()}@example.com`,
          password: 'password123',
          displayName: 'Test User Direct',
        }),
      });
      
      // Get the response data regardless of status
      const responseText = await response.text();
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (e) {
        data = { raw: responseText };
      }
      
      // Format the result with status information
      setResult(
        `Direct backend test - Status: ${response.status} ${response.statusText}\n\n` +
        `Response:\n${JSON.stringify(data, null, 2)}`
      );
    } catch (error) {
      console.error('Direct backend test error:', error);
      setResult(`Direct backend error: ${error instanceof Error ? error.message : String(error)}`);
    } finally {
      setLoading(false);
    }
  };

  const handleManualSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setResult('');
    
    try {
      const response = await fetch('/api/users', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          email,
          password: 'password123',
          displayName: 'Test User',
        }),
      });
      
      // Get the response data regardless of status
      const responseText = await response.text();
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (e) {
        data = { raw: responseText };
      }
      
      // Format the result with status information
      setResult(
        `Status: ${response.status} ${response.statusText}\n\n` +
        `Response:\n${JSON.stringify(data, null, 2)}`
      );
    } catch (error) {
      console.error('API test error:', error);
      setResult(`Error: ${error instanceof Error ? error.message : String(error)}`);
    } finally {
      setLoading(false);
    }
  };

  const testNextjsApi = async () => {
    setLoading(true);
    setResult('');
    try {
      // Test the Next.js API route
      const response = await fetch('/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: `testuser${Date.now()}`,
          email: `testuser${Date.now()}@example.com`,
          password: 'password123',
          displayName: 'Test User via Next.js API',
        }),
      });
      
      // Get the response data regardless of status
      const responseText = await response.text();
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (e) {
        data = { raw: responseText };
      }
      
      // Format the result with status information
      setResult(
        `Next.js API route test - Status: ${response.status} ${response.statusText}\n\n` +
        `Response:\n${JSON.stringify(data, null, 2)}`
      );
    } catch (error) {
      console.error('Next.js API route test error:', error);
      setResult(`Next.js API error: ${error instanceof Error ? error.message : String(error)}`);
    } finally {
      setLoading(false);
    }
  };
  
  const testHealthApi = async () => {
    setLoading(true);
    setResult('');
    try {
      // Test the Next.js health API route
      const response = await fetch('/api/health');
      
      // Get the response data regardless of status
      const responseText = await response.text();
      let data;
      try {
        data = JSON.parse(responseText);
      } catch (e) {
        data = { raw: responseText };
      }
      
      // Format the result with status information
      setResult(
        `Health API check - Status: ${response.status} ${response.statusText}\n\n` +
        `Response:\n${JSON.stringify(data, null, 2)}`
      );
    } catch (error) {
      console.error('Health API check error:', error);
      setResult(`Health API error: ${error instanceof Error ? error.message : String(error)}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <div className="mb-6">
        <h1 className="text-3xl font-bold mb-2">API Testing Page</h1>
        <p className="text-gray-600">
          Use this page to test API connectivity and registration functionality
        </p>
        <div className="mt-4">
          <Link href="/" className="text-blue-500 hover:underline">
            Back to Home
          </Link>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 mb-8">
        <div className="p-4 border rounded bg-gray-50">
          <h2 className="text-xl font-semibold mb-4">Quick Actions</h2>
          <div className="flex flex-wrap gap-3">
            <button
              className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
              onClick={testRegistration}
              disabled={loading}
            >
              {loading ? 'Testing...' : 'Test Registration (Random User)'}
            </button>
            
            <button
              className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
              onClick={testBackendHealth}
              disabled={loading}
            >
              {loading ? 'Testing...' : 'Test Backend Health'}
            </button>
            
            <button
              className="px-4 py-2 bg-purple-500 text-white rounded hover:bg-purple-600"
              onClick={testBackendDirect}
              disabled={loading}
            >
              {loading ? 'Testing...' : 'Test Direct Backend URL'}
            </button>
            
            <button
              className="px-4 py-2 bg-yellow-500 text-white rounded hover:bg-yellow-600"
              onClick={testNextjsApi}
              disabled={loading}
            >
              {loading ? 'Testing...' : 'Test Next.js API Route'}
            </button>
            
            <button
              className="px-4 py-2 bg-pink-500 text-white rounded hover:bg-pink-600" 
              onClick={testHealthApi}
              disabled={loading}
            >
              {loading ? 'Testing...' : 'Check Health API'}
            </button>
          </div>
        </div>
        
        <div className="p-4 border rounded bg-gray-50">
          <h2 className="text-xl font-semibold mb-4">Manual User Registration Test</h2>
          <form onSubmit={handleManualSubmit} className="space-y-3">
            <div>
              <label className="block text-sm font-medium mb-1" htmlFor="username">Username:</label>
              <input 
                type="text" 
                value={username} 
                onChange={(e) => setUsername(e.target.value)}
                className="w-full px-3 py-2 border rounded"
                required
                title="Username"
                placeholder="Enter username"
                id="username"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1" htmlFor="email">Email:</label>
              <input 
                type="email" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-3 py-2 border rounded"
                required
                title="Email"
                placeholder="Enter email address"
                id="email"
              />
            </div>
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600"
              disabled={loading}
            >
              {loading ? 'Testing...' : 'Test with Custom Data'}
            </button>
          </form>
        </div>
      </div>
      
      {result && (
        <div className="p-4 border rounded bg-gray-50">
          <h2 className="text-xl font-semibold mb-2">Test Result:</h2>
          <pre className="bg-white p-4 rounded border overflow-auto max-h-96">
            {result}
          </pre>
        </div>
      )}
    </div>
  );
} 