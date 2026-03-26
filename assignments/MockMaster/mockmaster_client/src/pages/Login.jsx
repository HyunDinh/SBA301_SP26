import LoginHeader from '../components/auth/LoginHeader';
import LoginForm from '../components/auth/LoginForm';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white p-10 rounded-2xl shadow-xl">
        <LoginHeader />

        <LoginForm
          onSuccess={(role) => {
            // Cập nhật: Cả Admin và User đều vào chung trang /dashboard 
            // vì logic phân quyền đã được xử lý bên trong file Dashboard.jsx
            if (role === 'ADMIN') {
              console.log('Admin logged in, redirecting to system overview...');
            }
            
            navigate('/dashboard');
          }}
          onRegisterClick={() => navigate('/register')}
        />
      </div>
    </div>
  );
}