'use client';

import { useState, useEffect } from 'react';
import { loginUser } from '@/services/api';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function Login() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  // This will help us debug the component's lifecycle
  useEffect(() => {
    console.log('Login component mounted');
    return () => console.log('Login component unmounted');
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    
    console.log(`Submitting login form for user: ${formData.username}`);

    try {
      const response = await loginUser(formData.username, formData.password);
      console.log('Login response from API:', response);
      
      // Check for token in different possible response formats
      if (response && response.status === 'success' && response.data && response.data.token) {
        // Format from our backend
        localStorage.setItem('authToken', response.data.token);
        console.log('Login successful, redirecting to home');
        router.push('/');
        return;
      } 
      
      if (response && response.data && response.data.token) {
        // Alternative format where token is directly in data
        localStorage.setItem('authToken', response.data.token);
        console.log('Login successful (alt format), redirecting to home');
        router.push('/');
        return;
      } 
      
      if (response && response.token) {
        // Simple format with token at root level
        localStorage.setItem('authToken', response.token);
        console.log('Login successful (simple format), redirecting to home');
        router.push('/');
        return;
      }
      
      // If we get here, we didn't find a token in the response
      console.error('Unexpected response format:', response);
      setError('Login succeeded but no token was found in the response. Please try again or contact support.');
    } catch (err: any) {
      console.error('Login error caught in component:', err);
      if (err.message) {
        setError(err.message);
      } else {
        setError('Failed to login. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen flex-col items-center justify-center p-4">
      <div className="w-full max-w-md p-6 bg-white rounded-lg shadow-md">
        <h1 className="text-3xl font-bold mb-6 text-center">Log in to Twitter Clone</h1>
        
        {error && (
          <div className="mb-4 p-3 bg-red-100 text-red-700 rounded-md">
            {error}
          </div>
        )}
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-1">
              Username
            </label>
            <input
              id="username"
              name="username"
              type="text"
              required
              value={formData.username}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter your username"
            />
          </div>
          
          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
              Password
            </label>
            <input
              id="password"
              name="password"
              type="password"
              required
              value={formData.password}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Enter your password"
            />
          </div>
          
          <div>
            <button
              type="submit"
              disabled={loading}
              className="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50"
            >
              {loading ? 'Logging in...' : 'Log In'}
            </button>
          </div>
        </form>
        
        <div className="mt-4 text-center">
          <p className="text-sm text-gray-600">
            Don't have an account?{' '}
            <Link href="/auth/signup" className="text-blue-500 hover:text-blue-700">
              Sign up
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
} 