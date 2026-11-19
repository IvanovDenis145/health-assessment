import { Link, useNavigate, useLocation } from 'react-router-dom';

export default function Layout({ children }) {
    const navigate = useNavigate();
    const location = useLocation();
    const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

    const userString = localStorage.getItem('user');
    const currentUser = userString ? JSON.parse(userString) : null;
    const isAdmin = currentUser?.role === 'ROLE_ADMIN';

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        navigate('/login');
    };

    return (
        <div className="d-flex flex-column min-vh-100">
            {!isAuthPage && (
                <nav className="navbar navbar-expand-lg navbar-light bg-white border-bottom shadow-sm">
                    <div className="container">
                        <Link className="navbar-brand fw-bold" to="/dashboard" style={{ color: '#d97706' }}>
                            🍊 Health Assessment
                        </Link>
                        <div className="d-flex align-items-center gap-3">
                            <Link to="/dashboard" className="btn btn-sm btn-outline-dark">Главная</Link>
                            {isAdmin && (
                                <Link to="/admin/users" className="btn btn-sm btn-outline-danger">
                                    👥 Пользователи
                                </Link>
                            )}
                            <button onClick={handleLogout} className="btn btn-sm btn-custom">Выйти</button>
                        </div>
                    </div>
                </nav>
            )}

            <div className="main-content">
                <div className="container">
                    {children}
                </div>
            </div>

            <footer className="text-center py-4 mt-auto border-top" style={{ backgroundColor: '#fff' }}>
                <p className="text-muted mb-0">&copy; 2026 Система оценки качества здоровья. УлГТУ.</p>
            </footer>
        </div>
    );
}