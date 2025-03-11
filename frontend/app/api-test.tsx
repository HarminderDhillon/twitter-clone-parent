'use client';

import React, { useState } from 'react';

export default function ApiTest() {
  const [result, setResult] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [username, setUsername] = useState<string>(`testuser${Date.now()}`);
  const [email, setEmail] = useState<string>(`testuser${Date.now()}@example.com`);

  const testApi = async () => {
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

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">API Test</h1>
      <div className="flex space-x-4 mb-4">
        <button
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
          onClick={testApi}
          disabled={loading}
        >
          {loading ? 'Testing...' : 'Test with Random User'}
        </button>
      </div>
      
      <div className="mb-6 p-4 border rounded">
        <h2 className="text-xl font-semibold mb-2">Manual User Registration Test</h2>
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
            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
            disabled={loading}
          >
            {loading ? 'Testing...' : 'Test with Custom Data'}
          </button>
        </form>
      </div>
      
      {result && (
        <div>
          <h2 className="text-xl font-semibold mb-2">Test Result:</h2>
          <pre className="bg-gray-100 p-4 rounded overflow-auto max-h-96">
            {result}
          </pre>
        </div>
      )}
    </div>
  );
} 