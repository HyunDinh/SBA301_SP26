import React, { useState } from "react";
import { Zap, CheckCircle2, Loader2, X } from "lucide-react";
import api from "../../services/api";

export default function UpgradeModal({ isOpen, onClose }) {
  const [loading, setLoading] = useState(false);

  const handleBuy = async () => {
    setLoading(true);
    try {
      const res = await api.post("/payments/create-url");
      // Redirect trực tiếp sang VNPay Sandbox
      window.location.href = res.data.paymentUrl;
    } catch (err) {
      alert("Lỗi khởi tạo thanh toán!");
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-slate-900/60 backdrop-blur-sm">
      <div className="bg-white rounded-[2.5rem] max-w-md w-full p-10 shadow-2xl relative animate-in zoom-in-95 duration-300">
        <button onClick={onClose} className="absolute right-6 top-6 text-slate-400 hover:text-slate-600">
          <X size={24} />
        </button>
        
        <div className="w-20 h-20 bg-amber-100 text-amber-600 rounded-[2rem] flex items-center justify-center mb-6 mx-auto shadow-inner">
          <Zap size={40} fill="currentColor" />
        </div>
        
        <h2 className="text-3xl font-black text-center italic uppercase tracking-tighter mb-2">MockMaster Premium</h2>
        <p className="text-slate-500 text-center text-sm mb-8 font-medium">
          Nâng cấp ngay để mở khóa toàn bộ sức mạnh công cụ mock API chuyên nghiệp!
        </p>
        
        <div className="space-y-4 mb-10">
          {[
            "Không giới hạn Workspace & API",
            "Cấu hình Response Delay (ms)", // Đặc quyền bạn yêu cầu
            "Hệ thống Firewall nâng cao",
            "Ưu tiên băng thông Server",
            "Hỗ trợ kỹ thuật 24/7"
          ].map((t) => (
            <div key={t} className="flex items-center gap-3 text-sm font-bold text-slate-700">
              <CheckCircle2 size={20} className="text-emerald-500" /> {t}
            </div>
          ))}
        </div>
        
        <button
          onClick={handleBuy}
          disabled={loading}
          className="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-5 rounded-[1.5rem] font-black flex items-center justify-center gap-3 shadow-lg shadow-indigo-200 transition-all active:scale-95 disabled:opacity-70"
        >
          {loading ? <Loader2 className="animate-spin" /> : "NÂNG CẤP NGAY - 100.000đ"}
        </button>
        
        <p className="text-[10px] text-slate-400 text-center mt-4 uppercase font-black tracking-widest">
          Secure payment via VNPay
        </p>
      </div>
    </div>
  );
}