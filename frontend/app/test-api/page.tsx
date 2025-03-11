'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';

export default function TestAPI() {
  const [testResult, setTestResult] = useState<string>('');
  const [loginResult, setLoginResult] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const testAPIRoute = async () => {
    setLoading(true);
    setError('');
    setTestResult('');
    
    try {
      console.log('Testing API route...');
      const response = await fetch('/api/test');
      const text = await response.text();
      console.log('API test response:', text);
      
      try {
        const data = JSON.parse(text);
        setTestResult(JSON.stringify(data, null, 2));
      } catch (e) {
        setTestResult(text);
      }
    } catch (err) {
      console.error('API test error:', err);
      setError(`Error testing API: ${err instanceof Error ? err.message : String(err)}`);
    } finally {
      setLoading(false);
    }
  };

  const testLoginAPI = async () => {
    setLoading(true);
    setError('');
    setLoginResult('');
    
    try {
      console.log('Testing login API directly...');
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: 'testuser',
          password: 'password123',
        }),
      });
      
      const text = await response.text();
      console.log('Login API test response:', text);
      
      try {
        const data = JSON.parse(text);
        setLoginResult(JSON.stringify(data, null, 2));
      } catch (e) {
        setLoginResult(text);
      }
    } catch (err) {
      console.error('Login API test error:', err);
      setError(`Error testing login API: ${err instanceof Error ? err.message : String(err)}`);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    console.log('Test API page loaded');
  }, []);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">API Test Page</h1>
      
      <div className="mb-4 flex space-x-2">
        <button
          onClick={testAPIRoute}
          disabled={loading}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:opacity-50"
        >
          Test API Route
        </button>
        
        <button
          onClick={testLoginAPI}
          disabled={loading}
          className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 disabled:opacity-50"
        >
          Test Login API
        </button>
        
        <Link href="/" className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600">
          Back to Home
        </Link>
      </div>
      
      {loading && <p className="text-blue-500">Loading...</p>}
      
      {error && (
        <div className="p-4 bg-red-100 text-red-700 rounded mb-4">
          <h2 className="font-bold">Error:</h2>
          <pre className="whitespace-pre-wrap">{error}</pre>
        </div>
      )}
      
      {testResult && (
        <div className="p-4 bg-green-100 text-green-700 rounded mb-4">
          <h2 className="font-bold">Test API Result:</h2>
          <pre className="whitespace-pre-wrap">{testResult}</pre>
        </div>
      )}
      
      {loginResult && (
        <div className="p-4 bg-blue-100 text-blue-700 rounded mb-4">
          <h2 className="font-bold">Login API Result:</h2>
          <pre className="whitespace-pre-wrap">{loginResult}</pre>
        </div>
      )}
      
      <div className="mt-8 p-4 bg-gray-100 rounded">
        <h2 className="font-bold mb-2">Debugging Information:</h2>
        <p>Current time: {new Date().toISOString()}</p>
        <p>Page URL: {typeof window !== 'undefined' ? window.location.href : 'Server-side rendering'}</p>
      </div>
    </div>
  );
} 