import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { userApi } from '../api/userApi';

export default function AdminUsersPage() {
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            const data = await userApi.getAllUsers();
            setUsers(data);
        } catch (err) {
            if (err.response?.status === 403) {
                alert('Доступ запрещен. Вы не администратор.');
                navigate('/dashboard');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const handleDelete = async (id, fio) => {
        if (window.confirm(`Удалить пользователя ${fio}? Все его показатели также удалятся.`)) {
            try {
                await userApi.deleteUser(id);
                loadUsers();
            } catch (err) {
                alert('Ошибка при удалении');
            }
        }
    };

    const handleEdit = async (user) => {
        const newFio = prompt('Введите новое ФИО:', user.fio);
        if (newFio && newFio !== user.fio) {
            try {
                await userApi.updateUser(user.id, { ...user, fio: newFio });
                loadUsers();
            } catch (err) {
                alert('Ошибка при обновлении');
            }
        }
    };

    if (isLoading) return <div className="text-center mt-5">Загрузка...</div>;

    return (
        <div className="py-3">
            <button className="btn btn-sm btn-outline-secondary mb-4" onClick={() => navigate('/dashboard')}>
                &larr; Назад на главную
            </button>

            <h2 className="mb-4" style={{ color: '#d97706' }}>Управление пользователями</h2>

            <div className="custom-card p-4">
                <div className="table-responsive">
                    <table className="table table-hover align-middle">
                        <thead className="table-light">
                        <tr>
                            <th>ФИО</th>
                            <th>Email</th>
                            <th>Роль</th>
                            <th>Возраст</th>
                            <th className="text-end">Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map(u => (
                            <tr key={u.id}>
                                <td>{u.fio}</td>
                                <td>{u.email}</td>
                                <td>
                                        <span className={`badge ${u.role === 'ROLE_ADMIN' ? 'bg-danger' : 'bg-secondary'}`}>
                                            {u.role === 'ROLE_ADMIN' ? 'Админ' : 'Пользователь'}
                                        </span>
                                </td>
                                <td>{u.age || '-'}</td>
                                <td className="text-end">
                                    <button className="btn btn-sm btn-outline-primary me-2" onClick={() => handleEdit(u)}>
                                        ✎ Ред.
                                    </button>
                                    <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(u.id, u.fio)}>
                                        ✕ Удалить
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}