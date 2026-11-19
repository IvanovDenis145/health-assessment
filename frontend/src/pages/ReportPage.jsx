import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { assessmentApi } from '../api/assessmentApi';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const COLORS = ['#ffd8a8', '#ffc078', '#ffb366', '#ffa64d', '#ff9933', '#ff8c1a', '#ff8000'];

export default function ReportPage() {
    const navigate = useNavigate();
    const [report, setReport] = useState(null);
    const [chartData, setChartData] = useState([]);
    const [tableData, setTableData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    const userString = localStorage.getItem('user');
    const currentUser = userString ? JSON.parse(userString) : null;
    const userId = currentUser?.id;

    useEffect(() => {
        if (!userId) {
            navigate('/login');
            return;
        }
        loadLatestReport();
    }, [userId, navigate]);

    const loadLatestReport = async () => {
        try {
            const history = await assessmentApi.getHistory(userId);
            if (history.length > 0) {
                const latest = history[0];
                setReport(latest);

                let detailsObj = {};
                try {
                    detailsObj = JSON.parse(latest.details || '{}');
                } catch (e) {
                    console.error("Ошибка парсинга", e);
                }

                const chart = detailsObj._chartData || {};
                const table = { ...detailsObj };
                delete table._chartData;

                // Форматируем данные для графика, округляя до 1 знака после запятой
                const formattedChartData = Object.entries(chart).map(([name, value]) => ({
                    name,
                    value: Math.round(parseFloat(value) * 10) / 10
                }));
                setChartData(formattedChartData);

                const formattedTableData = Object.entries(table).map(([key, value]) => ({
                    indicator: key,
                    value: value
                }));
                setTableData(formattedTableData);
            }
        } catch (err) {
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    const handlePrint = () => {
        window.print();
    };

    if (isLoading) return <div className="text-center mt-5">Загрузка отчёта...</div>;
    if (!report) return <div className="text-center mt-5">У вас пока нет сохранённых оценок.</div>;

    const getBadgeColor = (name) => {
        if (name === 'Здоровый') return 'bg-success';
        if (name === 'Среднее состояние') return 'bg-warning text-dark';
        return 'bg-danger';
    };

    return (
        <div className="py-3">
            <div className="d-flex justify-content-between align-items-center mb-4 d-print-none">
                <button className="btn btn-sm btn-outline-secondary" onClick={() => navigate('/dashboard')}>
                    &larr; Назад на главную
                </button>
                <button className="btn btn-custom" onClick={handlePrint}>
                    🖨️ Сохранить в PDF
                </button>
            </div>

            <div className="custom-card p-4 p-md-5">
                <div className="text-center mb-5 border-bottom pb-4">
                    <h2 style={{ color: '#d97706' }}>Отчёт об оценке состояния здоровья</h2>
                    <p className="text-muted mb-1">Пациент: <strong>{currentUser.fio}</strong></p>
                    <p className="text-muted">Дата: {new Date(report.createdAt).toLocaleString('ru-RU')}</p>
                </div>

                <div className={`alert ${getBadgeColor(report.name)} text-center mb-5 py-4`}>
                    <h1 className="display-4 fw-bold mb-0">{report.value} <span className="fs-3">/ 10</span></h1>
                    <h4 className="mb-0 mt-2">Категория: {report.name}</h4>
                </div>

                {/* Изменена сетка: таблица 5 колонок, график 7 колонок */}
                <div className="row g-4">
                    <div className="col-12 col-lg-5">
                        <h4 className="mb-3" style={{ color: '#d97706' }}>Использованные показатели</h4>
                        <div className="table-responsive">
                            <table className="table table-bordered table-striped table-sm">
                                <thead className="table-light">
                                <tr>
                                    <th>Показатель</th>
                                    <th>Значение</th>
                                </tr>
                                </thead>
                                <tbody>
                                {tableData.map((row, idx) => (
                                    <tr key={idx}>
                                        <td>{row.indicator}</td>
                                        <td><strong>{row.value}</strong></td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div className="col-12 col-lg-7 d-flex flex-column align-items-center">
                        <h4 className="mb-3" style={{ color: '#d97706' }}>Оценки по категориям</h4>
                        {/* Увеличен контейнер и добавлены отступы для графика */}
                        <div style={{ width: '100%', height: 400 }}>
                            <ResponsiveContainer>
                                <PieChart margin={{ top: 20, right: 40, bottom: 20, left: 40 }}>
                                    <Pie
                                        data={chartData}
                                        dataKey="value"
                                        nameKey="name"
                                        cx="50%"
                                        cy="50%"
                                        outerRadius={85} // Уменьшен радиус, чтобы влезли подписи
                                        labelLine={{ stroke: '#888', strokeWidth: 1 }}
                                        label={({ name, value }) => `${name}: ${value}`}
                                    >
                                        {chartData.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                        ))}
                                    </Pie>
                                    <Tooltip formatter={(value) => `${value} / 10`} />
                                    <Legend wrapperStyle={{ paddingTop: '20px' }} />
                                </PieChart>
                            </ResponsiveContainer>
                        </div>
                        <p className="text-muted small text-center mt-2">
                            * Диаграмма отображает баллы (от 0 до 10) по каждой из групп показателей.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}