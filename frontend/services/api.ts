import axios from 'axios';

// Create axios instance with default config
const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle 401 Unauthorized - redirect to login page
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('authToken');
      window.location.href = '/auth/login';
      return Promise.reject(error);
    }
    
    // Handle other errors
    return Promise.reject(error);
  }
);

// Auth APIs
export const loginUser = (username: string, password: string) => {
  return fetch('/api/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password }),
  }).then(response => {
    if (!response.ok) {
      return response.json().then(data => {
        throw new Error(data.message || `Error: ${response.status}`);
      });
    }
    return response.json();
  });
};

export const registerUser = async (userData: any) => {
  try {
    console.log('Registering user with data:', userData);
    const response = await fetch('/api/users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });

    if (!response.ok) {
      // Handle non-2xx responses
      const errorData = await response.json().catch(() => ({ message: 'Unknown error' }));
      console.error('Registration error response:', errorData);
      throw new Error(errorData.message || `Error: ${response.status}`);
    }
    
    const data = await response.json();
    console.log('Registration success:', data);
    return { data };
  } catch (error) {
    console.error('Registration error:', error);
    throw error;
  }
};

// User APIs
export const getCurrentUser = () => {
  return api.get('/users/me');
};

export const updateUserProfile = (userData: any) => {
  return api.put('/users/me', userData);
};

export const getUserProfile = (username: string) => {
  return api.get(`/users/${username}`);
};

export const followUser = (userId: number) => {
  return api.post(`/users/${userId}/follow`);
};

export const unfollowUser = (userId: number) => {
  return api.delete(`/users/${userId}/follow`);
};

// Post APIs
export const createPost = (content: string) => {
  return api.post('/posts', { content });
};

export const getPosts = (page = 0, size = 20) => {
  return api.get(`/posts?page=${page}&size=${size}`);
};

export const getUserPosts = (username: string, page = 0, size = 20) => {
  return api.get(`/users/${username}/posts?page=${page}&size=${size}`);
};

export const likePost = (postId: number) => {
  return api.post(`/posts/${postId}/like`);
};

export const unlikePost = (postId: number) => {
  return api.delete(`/posts/${postId}/like`);
};

export default api; 