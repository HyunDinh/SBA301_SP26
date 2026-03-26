import { logout } from "../../services/authService";
import { useNavigate } from "react-router-dom";
import { LogOut, LayoutGrid } from "lucide-react";

export default function Navbar({ email, planName }) {
  const navigate = useNavigate();

  return (
    <nav className="bg-white border-b border-slate-200 px-6 sm:px-8 py-3 flex justify-between items-center sticky top-0 z-40">
      <div className="flex items-center gap-3">
        <div className="w-10 h-10 bg-indigo-600 rounded-xl flex items-center justify-center text-white shadow">
          <LayoutGrid size={22} />
        </div>
        <span className="text-xl font-black tracking-tighter bg-gradient-to-r from-indigo-600 to-violet-600 bg-clip-text text-transparent uppercase">
          MockMaster
        </span>
      </div>
      <div className="flex items-center gap-4">
        <div className="hidden sm:block text-right border-r pr-4 border-slate-200">
          <p className="text-xs font-bold text-slate-800">{email || "Loading..."}</p>
          <span className="text-[10px] text-indigo-500 font-black uppercase tracking-wider">
            {planName || "FREE"}
          </span>
        </div>
        <button
          onClick={() => {
            logout();
            navigate("/login");
          }}
          className="p-2 text-slate-400 hover:text-red-500 transition-colors"
          title="Đăng xuất"
        >
          <LogOut size={20} />
        </button>
      </div>
    </nav>
  );
}