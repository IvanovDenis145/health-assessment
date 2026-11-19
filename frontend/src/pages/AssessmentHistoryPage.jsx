import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { assessmentApi } from '../api/assessmentApi';

export default function AssessmentHistoryPage() {
    const navigate = useNavigate();
    const [history, setHistory] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    const userString = localStorage.getItem('user');
    const currentUser = userString ? JSON.parse(userString) : null;
    const userId = currentUser?.id;

    useEffect(() => {
        if (!userId) {
            navigate('/login');
            return;
        }
        loadHistory();
    }, [userId, navigate]);

    const loadHistory = async () => {
        try {
            const data = await assessmentApi.getHistory(userId);
            setHistory(data);
        } catch (err) {
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    const getBadgeColor = (name) => {
        if (name === 'Здоровый') return 'bg-success';
        if (name === 'Среднее состояние') return 'bg-warning text-dark';
        return 'bg-danger';
    };

    if (isLoading) return <div className="text-center mt-5">Загрузка истории...</div>;

    return (
        <div className="py-3">
            <button className="btn btn-sm btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
                &larr; Назад на главную
            </button>

            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2 style={{ color: '#d97706' }}>История оценок здоровья</h2>
            </div>

            {history.length === 0 ? (
                <div className="custom-card p-5 text-center text-muted">
                    <h4>У вас пока нет оценок</h4>
                    <p>Пройдите оценку на главной странице, чтобы увидеть историю.</p>
                </div>
            ) : (
                <div className="row g-4">
                    {history.map((item) => {
                        // Парсим сохраненный JSON с деталями
                        let detailsObj = {};
                        try {
                            detailsObj = JSON.parse(item.details || '{}');
                            // Удаляем служебный объект, чтобы он не рендерился как текст
                            delete detailsObj._chartData;
                        } catch (e) {
                            console.error("Ошибка парсинга деталей", e);
                        }

                        return (
                            <div className="col-12 col-xl-6" key={item.id}>
                                <div className="custom-card p-4 h-100 d-flex flex-column">
                                    <div className="d-flex justify-content-between align-items-start mb-3 border-bottom pb-3">
                                        <div>
                                            <h4 className="mb-1">Оценка: {item.value} / 10</h4>
                                            <small className="text-muted">
                                                {new Date(item.createdAt).toLocaleString('ru-RU')}
                                            </small>
                                        </div>
                                        <span className={`badge ${getBadgeColor(item.name)} fs-6 px-3 py-2`}>
                                            {item.name}
                                        </span>
                                    </div>

                                    <h6 className="mb-2 text-muted">Использованные показатели:</h6>
                                    <ul className="list-group list-group-flush mb-3">
                                        {Object.entries(detailsObj).length > 0 ? (
                                            Object.entries(detailsObj).map(([key, val]) => (
                                                <li key={key} className="list-group-item px-0 py-2 d-flex justify-content-between align-items-center bg-transparent">
                                                    <span>{key}</span>
                                                    <strong>{typeof val === 'object' ? 'Сложные данные' : val}</strong>
                                                </li>
                                            ))
                                        ) : (
                                            <li className="list-group-item px-0 text-muted">Детали не сохранены (старая запись)</li>
                                        )}
                                    </ul>
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
}