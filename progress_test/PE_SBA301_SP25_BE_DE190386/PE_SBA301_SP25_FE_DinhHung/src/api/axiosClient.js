import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8080/',
  headers: {
    'Content-Type': 'application/json',
  }
});

axiosClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token && token !== "FAKE_TOKEN_FOR_NOW") {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default axiosClient;