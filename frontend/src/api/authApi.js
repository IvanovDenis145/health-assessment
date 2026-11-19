import apiClient from './client';

export const authApi = {
    login: async (email, password) => {
        const response = await apiClient.post('/auth/login', { email, password });
        return response.data; // { accessToken, tokenType, user }
    },
    register: async (userData) => {
        const response = await apiClient.post('/auth/register', userData);
        return response.data;
    }
};