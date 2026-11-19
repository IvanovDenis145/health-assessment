import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { indicatorApi } from '../api/indicatorApi';
import { INDICATOR_GROUPS } from '../config/indicatorConfig';

export default function IndicatorGroupPage() {
    const { groupKey } = useParams();
    const navigate = useNavigate();
    const group = INDICATOR_GROUPS[groupKey];

    const userString = localStorage.getItem('user');
    const currentUser = userString ? JSON.parse(userString) : null;
    const userId = currentUser?.id;

    if (!group) {
        return <div className="alert alert-danger mt-5">Группа показателей не найдена</div>;
    }

    return (
        <div className="py-3">
            <button className="btn btn-sm btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
                &larr; Назад на главную
            </button>

            <h2 className="mb-4" style={{ color: '#d97706' }}>
                {group.icon} {group.title}
            </h2>

            <div className="row g-4">
                {group.indicators.map(indicator => (
                    <div className="col-12" key={indicator.key}>
                        <IndicatorBlock
                            userId={userId}
                            indicator={indicator}
                        />
                    </div>
                ))}
            </div>
        </div>
    );
}

// Внутренний компонент для отдельного показателя (Рост, Вес и т.д.)
function IndicatorBlock({ userId, indicator }) {
    const [records, setRecords] = useState([]);
    const [formData, setFormData] = useState({});
    const [editingId, setEditingId] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        loadRecords();
    }, []);

    const loadRecords = async () => {
        try {
            const data = await indicatorApi.getAll(userId, indicator.key);
            setRecords(data);
        } catch (err) {
            console.error(err);
        }
    };

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        // Валидация
        for (const field of indicator.fields) {
            const val = formData[field.name];
            if (!val && val !== 0) {
                setError(`Поле "${field.label}" обязательно для заполнения`);
                return;
            }
            if (field.type === 'number') {
                const num = parseFloat(val);
                if (isNaN(num) || (field.min !== undefined && num < field.min) || (field.max !== undefined && num > field.max)) {
                    setError(`Поле "${field.label}" должно быть числом от ${field.min} до ${field.max}`);
                    return;
                }
            }
        }

        // Подготовка данных (числа конвертируем в числа)
        const submitData = { ...formData };
        indicator.fields.forEach(f => {
            if (f.type === 'number') submitData[f.name] = parseFloat(submitData[f.name]);
        });

        try {
            if (editingId) {
                await indicatorApi.update(userId, indicator.key, editingId, submitData);
            } else {
                await indicatorApi.create(userId, indicator.key, submitData);
            }
            setFormData({});
            setEditingId(null);
            loadRecords();
        } catch (err) {
            setError(err.response?.data?.message || 'Ошибка при сохранении');
        }
    };

    const handleEdit = (record) => {
        setEditingId(record.id);
        // Заполняем форму текущими значениями
        const editData = {};
        indicator.fields.forEach(f => editData[f.name] = record[f.name]);
        setFormData(editData);
        setError('');
    };

    const handleDelete = async (id) => {
        if (window.confirm('Удалить эту запись?')) {
            await indicatorApi.delete(userId, indicator.key, id);
            loadRecords();
        }
    };

    return (
        <div className="custom-card p-4">
            <h4 className="mb-4 border-bottom pb-2">{indicator.title}</h4>

            <div className="row g-4">
                {/* Форма */}
                <div className="col-12 col-lg-5">
                    <div className={`p-3 rounded ${editingId ? 'bg-light border border-warning' : ''}`}>
                        <h6 className="mb-3">{editingId ? 'Редактирование записи' : 'Добавить запись'}</h6>
                        <form onSubmit={handleSubmit}>
                            {indicator.fields.map(field => (
                                <div className="mb-3" key={field.name}>
                                    <label className="form-label">{field.label}</label>
                                    <div className="input-group">
                                        <input
                                            type={field.type}
                                            step={field.step}
                                            className="form-control"
                                            name={field.name}
                                            value={formData[field.name] || ''}
                                            onChange={handleInputChange}
                                            placeholder={field.type === 'number' ? `от ${field.min} до ${field.max}` : ''}
                                        />
                                        {indicator.unit && <span className="input-group-text">{indicator.unit}</span>}
                                    </div>
                                </div>
                            ))}

                            {error && <div className="text-danger small mb-2">{error}</div>}

                            <div className="d-flex gap-2">
                                <button type="submit" className="btn btn-custom flex-grow-1">
                                    {editingId ? 'Сохранить изменения' : 'Добавить'}
                                </button>
                                {editingId && (
                                    <button type="button" className="btn btn-outline-secondary" onClick={() => { setEditingId(null); setFormData({}); setError(''); }}>
                                        Отмена
                                    </button>
                                )}
                            </div>
                        </form>
                    </div>
                </div>

                {/* Таблица с данными */}
                <div className="col-12 col-lg-7">
                    <div className="table-responsive">
                        <table className="table table-hover align-middle">
                            <thead className="table-light">
                            <tr>
                                <th>Дата</th>
                                {indicator.fields.map(f => <th key={f.name}>{f.label}</th>)}
                                <th className="text-end">Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {records.length === 0 ? (
                                <tr><td colSpan={indicator.fields.length + 2} className="text-center text-muted py-4">Нет данных</td></tr>
                            ) : (
                                records.map(record => (
                                    <tr key={record.id} className={editingId === record.id ? 'table-warning' : ''}>
                                        <td><small>{new Date(record.createdAt).toLocaleDateString('ru-RU')}</small></td>
                                        {indicator.fields.map(f => (
                                            <td key={f.name}>{record[f.name]} {indicator.unit}</td>
                                        ))}
                                        <td className="text-end">
                                            <button className="btn btn-sm btn-outline-primary me-1" onClick={() => handleEdit(record)}>✎</button>
                                            <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(record.id)}>✕</button>
                                        </td>
                                    </tr>
                                ))
                            )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}