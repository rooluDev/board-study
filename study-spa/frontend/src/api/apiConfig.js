import axios from 'axios';

const baseUrl = 'http://localhost:8081/api';

export const api = axios.create({
  baseURL: baseUrl,
});

api.interceptors.request.use((config) => {
  return config;
});

api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    throw new Error(error);
  },
);
