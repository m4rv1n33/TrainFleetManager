import axios from 'axios';

const resolvedBaseURL = import.meta.env.VITE_API_BASE !== undefined && import.meta.env.VITE_API_BASE !== ''
  ? import.meta.env.VITE_API_BASE
  : import.meta.env.DEV
    ? '/api'
    : '';

const api = axios.create({
  baseURL: resolvedBaseURL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
