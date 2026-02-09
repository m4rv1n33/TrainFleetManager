import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import DashboardPage from './pages/Dashboard';
import TrainsPage from './pages/Trains';
import LinesPage from './pages/Lines';
import AssignmentsPage from './pages/Assignments';

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/trains" element={<TrainsPage />} />
        <Route path="/lines" element={<LinesPage />} />
        <Route path="/assignments" element={<AssignmentsPage />} />
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </Layout>
  );
}

export default App;
