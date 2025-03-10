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
  return api.post('/auth/login', { username, password });
};

export const registerUser = (userData: any) => {
  return api.post('/auth/register', userData);
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