import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import DashboardPage from './DashboardPage';
import { userApi } from '../api/userApi';
import { indicatorApi } from '../api/indicatorApi';

// Мокаем API
vi.mock('../api/userApi', () => ({
    userApi: { getUserById: vi.fn() }
}));
vi.mock('../api/indicatorApi', () => ({
    indicatorApi: { getAll: vi.fn() }
}));
vi.mock('../api/assessmentApi', () => ({
    assessmentApi: { evaluateHealth: vi.fn(), getHistory: vi.fn() }
}));

describe('DashboardPage', () => {
    beforeEach(() => {
        localStorage.setItem('user', JSON.stringify({ id: 'user-123', fio: 'Тест' }));

        // Имитируем, что пользователь есть, но показателей нет (пустые массивы)
        userApi.getUserById.mockResolvedValue({ id: 'user-123', fio: 'Тест', email: 'test@test.com' });
        indicatorApi.getAll.mockResolvedValue([]);
    });

    it('5. должен показывать предупреждение и блокировать кнопку, если не хватает данных', async () => {
        render(<BrowserRouter><DashboardPage /></BrowserRouter>);

        // Ждем загрузки данных (findByText)
        expect(await screen.findByText(/Карточка клиента/i)).toBeInTheDocument();

        // Проверяем, что вывелось предупреждение о нехватке данных
        expect(screen.getByText(/Недостаточно данных/i)).toBeInTheDocument();

        // Проверяем, что кнопка оценки заблокирована (disabled)
        const assessButton = screen.getByRole('button', { name: /Заполните показатели/i });
        expect(assessButton).toBeDisabled();
    });
});