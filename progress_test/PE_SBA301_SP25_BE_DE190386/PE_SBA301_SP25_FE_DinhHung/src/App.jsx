import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AppNavbar from './components/AppNavbar';
import CarManagement from './pages/CarManagement';
import { AuthProvider } from './context/AuthContext';

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppNavbar />
        <Routes>
          <Route path="/" element={<div className="container mt-5 text-center"><h1>Home Page - Welcome</h1></div>} />
          <Route path="/cars" element={<CarManagement />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;