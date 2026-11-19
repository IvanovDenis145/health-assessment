import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { userApi } from '../api/userApi';
import { indicatorApi } from '../api/indicatorApi';
import { assessmentApi } from '../api/assessmentApi';
import { INDICATOR_GROUPS } from '../config/indicatorConfig';

const REQUIRED_CATEGORIES = [
    { key: 'heights', name: 'Рост' },
    { key: 'weights', name: 'Вес' },
    { key: 'bloodPressures', name: 'Давление' },
    { key: 'heartRates', name: 'Пульс' },
    { key: 'glucoseLevels', name: 'Глюкоза' },
    { key: 'stressLevels', name: 'Стресс' },
    { key: 'subjectiveWellBeings', name: 'Самочувствие' }
];

export default function DashboardPage() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [missingIndicators, setMissingIndicators] = useState([]);
    const [isDataLoaded, setIsDataLoaded] = useState(false);
    const [isAssessing, setIsAssessing] = useState(false);

    const userString = localStorage.getItem('user');
    const currentUser = userString ? JSON.parse(userString) : null;
    const userId = currentUser?.id;

    useEffect(() => {
        if (!userId) {
            navigate('/login');
            return;
        }
        loadDashboardData();
    }, [userId, navigate]);

    const loadDashboardData = async () => {
        try {
            const userData = await userApi.getUserById(userId);
            setUser(userData);

            const missing = [];
            for (const cat of REQUIRED_CATEGORIES) {
                const data = await indicatorApi.getAll(userId, cat.key);
                if (!data || data.length === 0) {
                    missing.push(cat);
                }
            }
            setMissingIndicators(missing);
            setIsDataLoaded(true);
        } catch (err) {
            console.error(err);
            if (err.response?.status === 401 || err.response?.status === 403 || err.response?.status === 404) {
                localStorage.clear();
                navigate('/login');
            }
        }
    };

    const handleAssessHealth = async () => {
        setIsAssessing(true);
        try {
            await assessmentApi.evaluateHealth(userId);
            // После успешной оценки переходим на страницу истории
            navigate('/report');
        } catch (err) {
            alert('Ошибка при запуске алгоритма');
            setIsAssessing(false);
        }
    };

    if (!isDataLoaded || !user) return <div className="text-center mt-5">Загрузка данных...</div>;

    const isReadyForAssessment = missingIndicators.length === 0;

    return (
        <div className="py-3">
            <div className="row g-4">
                {/* Карточка клиента */}
                <div className="col-12 col-lg-5">
                    <div className="custom-card p-4 h-100">
                        <h3 className="mb-4" style={{ color: '#d97706' }}>Карточка клиента</h3>
                        <ul className="list-group list-group-flush">
                            <li className="list-group-item px-0 py-3"><strong>ФИО:</strong> {user.fio}</li>
                            <li className="list-group-item px-0 py-3"><strong>Email:</strong> {user.email}</li>
                            <li className="list-group-item px-0 py-3"><strong>Телефон:</strong> {user.phoneNumber || 'Не указан'}</li>
                            <li className="list-group-item px-0 py-3"><strong>Возраст:</strong> {user.age || 'Не указан'}</li>
                            <li className="list-group-item px-0 py-3"><strong>Пол:</strong> {user.gender === 'male' ? 'Мужской' : 'Женский'}</li>
                        </ul>
                    </div>
                </div>

                {/* Блок оценки здоровья */}
                <div className="col-12 col-lg-7">
                    <div className="custom-card p-4 h-100 d-flex flex-column">
                        <h3 className="mb-4" style={{ color: '#d97706' }}>Оценка здоровья</h3>

                        {!isReadyForAssessment ? (
                            <div className="alert alert-warning flex-grow-1">
                                <h5 className="alert-heading">Недостаточно данных</h5>
                                <p>Для запуска алгоритма необходимо заполнить показатели по всем категориям.</p>
                                <hr />
                                <p className="mb-2"><strong>Чего не хватает:</strong></p>
                                <ul className="mb-0">
                                    {missingIndicators.map(m => (
                                        <li key={m.key}>{m.name}</li>
                                    ))}
                                </ul>
                            </div>
                        ) : (
                            <div className="alert alert-success flex-grow-1 d-flex flex-column justify-content-center align-items-center">
                                <h4 className="mb-3">Все данные заполнены!</h4>
                                <p className="text-muted text-center mb-4">Вы можете запустить алгоритм интегральной оценки состояния здоровья.</p>
                            </div>
                        )}

                        <div className="d-flex gap-2 mt-3 flex-column flex-sm-row">
                            <button
                                className="btn btn-custom btn-lg flex-grow-1 py-3"
                                onClick={handleAssessHealth}
                                disabled={!isReadyForAssessment || isAssessing}
                            >
                                {isAssessing ? 'Оценка...' : (isReadyForAssessment ? 'Оценить здоровье' : 'Заполните показатели')}
                            </button>

                            <button
                                className="btn btn-outline-dark btn-lg py-3"
                                onClick={() => navigate('/report')}
                                title="Последний отчёт"
                            >
                                📊 Отчёт
                            </button>

                            <button
                                className="btn btn-outline-dark btn-lg py-3"
                                onClick={() => navigate('/assessment-history')}
                                title="История оценок"
                            >
                                📜 История
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            {/* Сетка с ссылками на все категории показателей */}
            <h3 className="mt-5 mb-4" style={{ color: '#d97706' }}>Категории показателей</h3>
            <div className="row g-3">
                {Object.entries(INDICATOR_GROUPS).map(([key, group]) => (
                    <div className="col-12 col-md-6 col-lg-4" key={key}>
                        <Link to={`/indicators/${key}`} className="text-decoration-none">
                            <div className="custom-card p-4 text-dark d-flex justify-content-between align-items-center hover-shadow h-100" style={{ transition: 'transform 0.2s' }} onMouseOver={(e) => e.currentTarget.style.transform = 'translateY(-3px)'} onMouseOut={(e) => e.currentTarget.style.transform = 'translateY(0)'}>
                                <div>
                                    <span className="fs-4 me-2">{group.icon}</span>
                                    <strong>{group.title}</strong>
                                </div>
                                <span className="fs-5" style={{ color: '#d97706' }}>&rarr;</span>
                            </div>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}