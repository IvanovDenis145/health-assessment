import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authApi } from '../api/authApi';

export default function RegisterPage() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        fio: '', email: '', password: '', phoneNumber: '', age: '', gender: 'male'
    });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!formData.password || formData.password.length < 6) {
            setError('Пароль должен быть не менее 6 символов');
            return;
        }

        try {
            await authApi.register(formData);
            alert('Регистрация успешна! Теперь войдите в систему.');
            navigate('/login');
        } catch (err) {
            setError(err.response?.data?.message || 'Ошибка при регистрации');
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: '500px' }}>
            <h2 className="mb-4">Регистрация</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit} noValidate>
                <div className="mb-3">
                    <label htmlFor="fio" className="form-label">ФИО *</label>
                    <input id="fio" type="text" className="form-control" name="fio" onChange={handleChange} required />
                </div>
                <div className="row">
                    <div className="col-md-6 mb-3">
                        <label htmlFor="email" className="form-label">Email *</label>
                        <input id="email" type="email" className="form-control" name="email" onChange={handleChange} required />
                    </div>
                    <div className="col-md-6 mb-3">
                        <label htmlFor="password" className="form-label">Пароль *</label>
                        <input id="password" type="password" className="form-control" name="password" onChange={handleChange} required minLength="6" />
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-6 mb-3">
                        <label htmlFor="phoneNumber" className="form-label">Телефон</label>
                        <input id="phoneNumber" type="tel" className="form-control" name="phoneNumber" onChange={handleChange} placeholder="79001234567" />
                    </div>
                    <div className="col-md-6 mb-3">
                        <label htmlFor="age" className="form-label">Возраст</label>
                        <input id="age" type="number" className="form-control" name="age" onChange={handleChange} min="1" max="120" />
                    </div>
                </div>
                <div className="mb-4">
                    <label htmlFor="gender" className="form-label">Пол</label>
                    <select id="gender" className="form-select" name="gender" onChange={handleChange}>
                        <option value="male">Мужской</option>
                        <option value="female">Женский</option>
                    </select>
                </div>
                <button type="submit" className="btn btn-custom w-100 py-2 mb-3">Зарегистрироваться</button>
            </form>
            <div className="text-center mt-3">
                <span className="text-muted">Уже есть аккаунт? </span>
                <Link to="/login">Войти</Link>
            </div>
        </div>
    );
}