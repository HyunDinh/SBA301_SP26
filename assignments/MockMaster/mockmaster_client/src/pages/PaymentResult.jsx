import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import { CheckCircle, XCircle, Loader2 } from "lucide-react";

export default function PaymentResult() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [status, setStatus] = useState("loading");

  useEffect(() => {
    const verifyPayment = async () => {
      try {
        // 1. Chuyển searchParams thành Object
        const params = Object.fromEntries(searchParams.entries());
        
        // 2. Gọi Backend để xác thực giao dịch và cập nhật Rank
        // Backend sẽ kiểm tra checksum và cập nhật User sang PREMIUM
        const response = await api.get("/payments/vnpay-callback", { params });

        // 3. Kiểm tra mã phản hồi từ VNPay (00 là thành công)
        if (params.vnp_ResponseCode === "00") {
          setStatus("success");
        } else {
          setStatus("failed");
        }
      } catch (error) {
        console.error("Lỗi xác thực thanh toán:", error);
        setStatus("failed");
      }
    };

    verifyPayment();
  }, [searchParams]);

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-6 text-center font-sans">
      <div className="bg-white p-10 rounded-[3rem] shadow-xl max-w-md w-full border border-slate-100 animate-in zoom-in-95 duration-500">
        {status === "loading" ? (
          <div className="flex flex-col items-center">
            <Loader2 className="animate-spin mb-6 text-indigo-600" size={48}/> 
            <h2 className="text-xl font-bold text-slate-800">Đang xác thực giao dịch...</h2>
            <p className="text-slate-500 text-sm mt-2">Vui lòng không đóng trình duyệt</p>
          </div>
        ) : status === "success" ? (
          <>
            <div className="w-20 h-20 bg-emerald-100 text-emerald-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <CheckCircle size={48} />
            </div>
            <h1 className="text-2xl font-black italic uppercase text-slate-900 tracking-tighter">Thanh toán thành công!</h1>
            <p className="text-slate-500 mt-2 mb-8 text-sm font-medium">
              Tài khoản của bạn đã được nâng cấp lên gói <span className="text-indigo-600 font-bold">PREMIUM</span>. 
              Bây giờ bạn có thể tạo không giới hạn Workspace và sử dụng tính năng Delay.
            </p>
            <button 
              onClick={() => navigate("/dashboard")} 
              className="w-full bg-indigo-600 hover:bg-indigo-700 text-white py-4 rounded-2xl font-bold shadow-lg shadow-indigo-100 transition-all active:scale-95"
            >
              VỀ DASHBOARD NGAY
            </button>
          </>
        ) : (
          <>
            <div className="w-20 h-20 bg-rose-100 text-rose-500 rounded-full flex items-center justify-center mx-auto mb-6">
              <XCircle size={48} />
            </div>
            <h1 className="text-2xl font-black italic uppercase text-slate-900 tracking-tighter">Giao dịch thất bại</h1>
            <p className="text-slate-500 mt-2 mb-8 text-sm font-medium">
              Có lỗi xảy ra trong quá trình thanh toán hoặc giao dịch đã bị hủy.
            </p>
            <button 
              onClick={() => navigate("/dashboard")} 
              className="w-full bg-slate-100 hover:bg-slate-200 text-slate-600 py-4 rounded-2xl font-bold transition-all active:scale-95"
            >
              QUAY LẠI THỬ LẠI
            </button>
          </>
        )}
      </div>
    </div>
  );
}