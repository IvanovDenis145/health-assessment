import apiClient from './client';

export const userApi = {
    getUserById: async (id) => {
        const response = await apiClient.get(`/users/${id}`);
        return response.data;
    },
    getAllUsers: async () => {
        const response = await apiClient.get('/users');
        return response.data;
    },
    deleteUser: async (id) => {
        await apiClient.delete(`/users/${id}`);
    },
    updateUser: async (id, data) => {
        const response = await apiClient.put(`/users/${id}`, data);
        return response.data;
    }
};