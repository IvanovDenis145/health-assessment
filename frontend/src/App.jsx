import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import IndicatorGroupPage from './pages/IndicatorGroupPage';
import AssessmentHistoryPage from './pages/AssessmentHistoryPage';
import ReportPage from './pages/ReportPage';
import AdminUsersPage from './pages/AdminUsersPage';

export default function App() {
    return (
        <BrowserRouter future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
            <Layout>
                <Routes>
                    <Route path="/" element={<Navigate to="/dashboard" replace />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/dashboard" element={<DashboardPage />} />
                    <Route path="/assessment-history" element={<AssessmentHistoryPage />} />
                    <Route path="/report" element={<ReportPage />} />
                    <Route path="/admin/users" element={<AdminUsersPage />} />
                    {/* Универсальный роут для всех 7 групп */}
                    <Route path="/indicators/:groupKey" element={<IndicatorGroupPage />} />
                </Routes>
            </Layout>
        </BrowserRouter>
    );
}