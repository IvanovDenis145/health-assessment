import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { describe, it, expect } from 'vitest';
import LoginPage from './LoginPage';

describe('LoginPage', () => {
    it('должен отрисовывать поля ввода и кнопку', () => {
        render(
            <BrowserRouter>
                <LoginPage />
            </BrowserRouter>
        );

        expect(screen.getByLabelText(/Email/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Пароль/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Войти/i })).toBeInTheDocument();
    });
});