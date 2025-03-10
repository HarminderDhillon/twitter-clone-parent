'use client';

import { useEffect, useState } from 'react';

export default function Home() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if the user is authenticated
    const token = localStorage.getItem('authToken');
    if (token) {
      setIsAuthenticated(true);
      // Fetch posts if authenticated
      fetchPosts(token);
    } else {
      setLoading(false);
    }
  }, []);

  const fetchPosts = async (token: string) => {
    try {
      const response = await fetch('/api/posts', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      
      if (response.ok) {
        const data = await response.json();
        setPosts(data);
      }
    } catch (error) {
      console.error('Error fetching posts:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = () => {
    // Redirect to login page
    window.location.href = '/auth/login';
  };

  const handleSignup = () => {
    // Redirect to signup page
    window.location.href = '/auth/signup';
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <main className="flex min-h-screen flex-col items-center p-4 md:p-24">
      <div className="w-full max-w-5xl">
        {isAuthenticated ? (
          <div>
            <h1 className="text-4xl font-bold mb-8">Your Feed</h1>
            {posts.length > 0 ? (
              <div className="space-y-4">
                {posts.map((post: any) => (
                  <div key={post.id} className="p-4 border rounded-lg shadow-sm">
                    <p className="font-bold">{post.user?.username || 'Unknown User'}</p>
                    <p>{post.content}</p>
                    <p className="text-sm text-gray-500 mt-2">
                      {new Date(post.createdAt).toLocaleString()}
                    </p>
                  </div>
                ))}
              </div>
            ) : (
              <p>No posts to display. Follow users to see their posts!</p>
            )}
          </div>
        ) : (
          <div className="text-center">
            <h1 className="text-4xl font-bold mb-8">Welcome to Twitter Clone</h1>
            <p className="text-xl mb-8">Connect with friends and the world around you.</p>
            <div className="space-x-4">
              <button 
                onClick={handleLogin}
                className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-6 rounded-full"
              >
                Log In
              </button>
              <button 
                onClick={handleSignup}
                className="bg-white hover:bg-gray-100 text-blue-500 font-bold py-2 px-6 rounded-full border border-blue-500"
              >
                Sign Up
              </button>
            </div>
          </div>
        )}
      </div>
    </main>
  );
} 