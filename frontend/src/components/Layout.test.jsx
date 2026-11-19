import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { describe, it, expect, beforeEach } from 'vitest';
import Layout from './Layout';

describe('Layout (Роли и навигация)', () => {
    beforeEach(() => {
        localStorage.clear();
    });

    it('3. должен скрывать кнопку админа для обычного пользователя', () => {
        // Имитируем обычного юзера в localStorage
        localStorage.setItem('user', JSON.stringify({ id: '1', role: 'ROLE_USER' }));

        render(<BrowserRouter><Layout><div>Контент</div></Layout></BrowserRouter>);

        // Кнопки "Пользователи" быть не должно
        expect(screen.queryByText(/Пользователи/i)).not.toBeInTheDocument();
        expect(screen.getByText(/Главная/i)).toBeInTheDocument();
    });

    it('4. должен показывать кнопку админа для администратора', () => {
        // Имитируем админа в localStorage
        localStorage.setItem('user', JSON.stringify({ id: '1', role: 'ROLE_ADMIN' }));

        render(<BrowserRouter><Layout><div>Контент</div></Layout></BrowserRouter>);

        // Кнопка "Пользователи" должна появиться
        expect(screen.getByText(/👥 Пользователи/i)).toBeInTheDocument();
    });
});