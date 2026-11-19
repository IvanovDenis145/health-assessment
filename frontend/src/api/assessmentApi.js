import apiClient from './client';

export const assessmentApi = {
    evaluateHealth: async (userId) => {
        const response = await apiClient.post(`/users/${userId}/assessment`);
        return response.data;
    },
    getHistory: async (userId) => {
        const response = await apiClient.get(`/users/${userId}/assessment`);
        return response.data;
    }
};