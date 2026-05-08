import axios from 'axios';

// Create axios instance with base URL
const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
});

// Add token to request headers
API.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

/**
 * Authentication API calls
 */
export const authAPI = {
  register: (data) => API.post('/auth/register', data),
  login: (data) => API.post('/auth/login', data),
};

/**
 * Wallet API calls
 */
export const walletAPI = {
  getBalance: () => API.get('/wallet/balance'),
  getWalletDetails: () => API.get('/wallet/details'),
  addMoney: (amount) => API.post('/wallet/add-money', { amount }),
};

/**
 * Transaction API calls
 */
export const transactionAPI = {
  sendMoney: (data) => API.post('/transactions/send-money', data),
  requestMoney: (data) => API.post('/transactions/request-money', data),
  getHistory: () => API.get('/transactions/history'),
  getTransaction: (refId) => API.get(`/transactions/${refId}`),
};

export default API;
