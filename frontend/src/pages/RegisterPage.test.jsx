import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { BrowserRouter } from 'react-router-dom';
import { describe, it, expect, vi } from 'vitest';
import RegisterPage from './RegisterPage';

vi.mock('../api/authApi', () => ({
    authApi: { register: vi.fn() }
}));

describe('RegisterPage', () => {
    it('1. должен отрисовывать все поля формы', () => {
        render(<BrowserRouter><RegisterPage /></BrowserRouter>);

        expect(screen.getByLabelText(/ФИО \*/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Email \*/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Пароль \*/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Телефон/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Возраст/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Зарегистрироваться/i })).toBeInTheDocument();
    });

    it('2. должен показывать ошибку, если пароль короче 6 символов', async () => {
        const user = userEvent.setup();
        render(<BrowserRouter><RegisterPage /></BrowserRouter>);

        // Заполняем обязательные поля
        await user.type(screen.getByLabelText(/ФИО \*/i), 'Иванов Иван');
        await user.type(screen.getByLabelText(/Email \*/i), 'test@test.com');
        await user.type(screen.getByLabelText(/Пароль \*/i), '123'); // Короткий пароль

        // Нажимаем кнопку
        await user.click(screen.getByRole('button', { name: /Зарегистрироваться/i }));

        // Проверяем, что появилась ошибка валидации
        expect(await screen.findByText(/Пароль должен быть не менее 6 символов/i)).toBeInTheDocument();
    });
});