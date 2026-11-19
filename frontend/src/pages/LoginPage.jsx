import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authApi } from '../api/authApi';

export default function LoginPage() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        if (!email || !password) {
            setError('Заполните все поля');
            return;
        }
        try {
            const data = await authApi.login(email, password);
            localStorage.setItem('token', data.accessToken);
            localStorage.setItem('user', JSON.stringify(data.user));
            navigate('/dashboard');
        } catch (err) {
            setError(err.response?.data?.message || 'Неверный email или пароль');
        }
    };

    return (
        <div className="row justify-content-center align-items-center" style={{ minHeight: '70vh' }}>
            <div className="col-12 col-md-8 col-lg-5">
                <div className="custom-card p-4 p-md-5">
                    <h2 className="text-center mb-4" style={{ color: '#d97706' }}>Вход в систему</h2>
                    {error && <div className="alert alert-danger py-2">{error}</div>}
                    <form onSubmit={handleSubmit} noValidate>
                        <div className="mb-3">
                            {/* ДОБАВИТЬ htmlFor="email" */}
                            <label htmlFor="email" className="form-label">Email</label>
                            <input
                                id="email" /* ДОБАВИТЬ id="email" */
                                type="email"
                                className={`form-control ${error && !email ? 'is-invalid' : ''}`}
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        <div className="mb-4">
                            {/* ДОБАВИТЬ htmlFor="password" */}
                            <label htmlFor="password" className="form-label">Пароль</label>
                            <input
                                id="password" /* ДОБАВИТЬ id="password" */
                                type="password"
                                className={`form-control ${error && !password ? 'is-invalid' : ''}`}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                        <button type="submit" className="btn btn-custom w-100 py-2 mb-3">Войти</button>
                    </form>
                    <div className="text-center">
                        <span className="text-muted">Нет аккаунта? </span>
                        <Link to="/register" style={{ color: '#d97706' }}>Зарегистрироваться</Link>
                    </div>
                </div>
            </div>
        </div>
    );
}