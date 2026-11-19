import apiClient from './client';

// Словарь путей (как они прописаны в контроллерах бэкенда)
const paths = {
    heights: 'heights',
    weights: 'weights',
    chestCircumferences: 'chest-circumferences',
    skinFolds: 'skin-folds',
    vitalLungCapacities: 'vital-lung-capacities',
    dynamometries: 'dynamometries',
    deadliftStrengths: 'deadlift-strengths',
    heartRates: 'heart-rates',
    bloodPressures: 'blood-pressures',
    respiratoryRates: 'respiratory-rates',
    glucoseLevels: 'glucose-levels',
    cholesterolLevels: 'cholesterol-levels',
    triglycerideLevels: 'triglyceride-levels',
    immunoglobulinLevels: 'immunoglobulin-levels',
    phagocyticIndexes: 'phagocytic-indexes',
    stressLevels: 'stress-levels',
    mentalDisorders: 'mental-disorders',
    subjectiveWellBeings: 'subjective-well-beings',
    stateOfHealths: 'state-of-healths'
};

export const indicatorApi = {
    getAll: async (userId, type) => {
        const response = await apiClient.get(`/users/${userId}/${paths[type]}`);
        return response.data;
    },
    create: async (userId, type, data) => {
        const response = await apiClient.post(`/users/${userId}/${paths[type]}`, data);
        return response.data;
    },
    update: async (userId, type, id, data) => {
        const response = await apiClient.put(`/users/${userId}/${paths[type]}/${id}`, data);
        return response.data;
    },
    delete: async (userId, type, id) => {
        await apiClient.delete(`/users/${userId}/${paths[type]}/${id}`);
    }
};