import useLogin from '../../hooks/useLogin';
import ErrorAlert from './ErrorAlert';
import SpinnerButton from './SpinnerButton';

export default function LoginForm({ onSuccess, onRegisterClick }) {
  const { email, setEmail, password, setPassword, error, loading, handleLogin } = useLogin();

  const onSubmit = async (e) => {
    const role = await handleLogin(e);
    if (role) {
      onSuccess(role);
    }
  };

  return (
    <form className="mt-8 space-y-6" onSubmit={onSubmit}>
      {error && <ErrorAlert message={error} />}

      <div className="rounded-md shadow-sm space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Email của bạn
          </label>
          <input
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="appearance-none relative block w-full px-3 py-3 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-lg focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
            placeholder="dinhhung@example.com"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Mật khẩu
          </label>
          <input
            type="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="appearance-none relative block w-full px-3 py-3 border border-gray-300 placeholder-gray-400 text-gray-900 rounded-lg focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
            placeholder="••••••••"
          />
        </div>
      </div>

      <div className="flex items-center justify-between">
        <div className="flex items-center">
          <input
            id="remember-me"
            type="checkbox"
            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
          />
          <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
            Ghi nhớ tôi
          </label>
        </div>
        <div className="text-sm">
          <a href="#" className="font-medium text-blue-600 hover:text-blue-500">
            Quên mật khẩu?
          </a>
        </div>
      </div>

      <SpinnerButton loading={loading} text="Đăng nhập" />

      <div className="text-center mt-4">
        <p className="text-sm text-gray-600">
          Chưa có tài khoản?{' '}
          <button
            type="button"
            onClick={onRegisterClick}
            className="font-medium text-blue-600 hover:text-blue-500"
          >
            Đăng ký dùng thử
          </button>
        </p>
      </div>
    </form>
  );
}