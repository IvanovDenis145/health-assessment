import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { userApi } from '../api/userApi';
import { indicatorApi } from '../api/indicatorApi';
import { assessmentApi } from '../api/assessmentApi';

export default function ProfilePage() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [heights, setHeights] = useState([]);
    const [newHeight, setNewHeight] = useState('');
    const [assessmentResult, setAssessmentResult] = useState(null);

    // Получаем пользователя из localStorage
    const userString = localStorage.getItem('user');
    const currentUser = userString ? JSON.parse(userString) : null;
    const userId = currentUser?.id;

    useEffect(() => {
        if (!userId) {
            navigate('/login');
            return;
        }
        loadData();
    }, [userId, navigate]);

    const loadData = async () => {
        try {
            const userData = await userApi.getUserById(userId);
            setUser(userData);

            // Используем универсальный API
            const heightData = await indicatorApi.getAll(userId, 'heights');
            setHeights(heightData);
        } catch (err) {
            console.error(err);
            if (err.response?.status === 401 || err.response?.status === 403) {
                handleLogout();
            }
        }
    };

    const handleAddHeight = async (e) => {
        e.preventDefault();
        if (!newHeight) return;
        try {
            await indicatorApi.create(userId, 'heights', { value: parseInt(newHeight) });
            setNewHeight('');
            loadData();
        } catch (err) {
            alert('Ошибка при добавлении роста');
        }
    };

    const handleAssessHealth = async () => {
        try {
            const result = await assessmentApi.evaluateHealth(userId);
            setAssessmentResult(result);
        } catch (err) {
            alert('Ошибка при запуске алгоритма оценки');
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
    };

    if (!user) return <div className="text-center mt-5">Загрузка...</div>;

    return (
        <div className="container mt-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Карточка пользователя</h2>
                <button className="btn btn-outline-danger" onClick={handleLogout}>Выйти</button>
            </div>

            {/* Блок с оценкой здоровья */}
            {assessmentResult && (
                <div className={`alert ${assessmentResult.name === 'Здоровый' ? 'alert-success' : assessmentResult.name === 'Среднее состояние' ? 'alert-warning' : 'alert-danger'} mb-4`}>
                    <h4 className="alert-heading">Результат оценки</h4>
                    <p className="mb-1"><strong>Балл:</strong> {assessmentResult.value} / 10</p>
                    <p className="mb-0"><strong>Категория:</strong> {assessmentResult.name}</p>
                </div>
            )}

            <div className="card mb-4">
                <div className="card-body d-flex justify-content-between align-items-center">
                    <div>
                        <h5 className="card-title">{user.fio}</h5>
                        <p className="card-text mb-1"><strong>Email:</strong> {user.email}</p>
                        <p className="card-text mb-1"><strong>Возраст:</strong> {user.age || 'Не указан'}</p>
                        <p className="card-text mb-0"><strong>Пол:</strong> {user.gender === 'male' ? 'Мужской' : 'Женский'}</p>
                    </div>
                    <button className="btn btn-primary btn-lg" onClick={handleAssessHealth}>
                        Оценить здоровье
                    </button>
                </div>
            </div>

            <h4>Рост (Антропометрия)</h4>
            <div className="row">
                <div className="col-md-6 mb-3">
                    <ul className="list-group">
                        {heights.length === 0 ? <li className="list-group-item">Нет данных</li> : null}
                        {heights.map((h, index) => (
                            <li key={h.id} className="list-group-item d-flex justify-content-between align-items-center">
                                Запись {index + 1}: <span className="badge bg-primary rounded-pill">{h.value} см</span>
                            </li>
                        ))}
                    </ul>
                </div>
                <div className="col-md-6">
                    <form onSubmit={handleAddHeight} className="input-group">
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Новый рост (см)"
                            value={newHeight}
                            onChange={(e) => setNewHeight(e.target.value)}
                        />
                        <button className="btn btn-success" type="submit">Добавить</button>
                    </form>
                </div>
            </div>
        </div>
    );
}