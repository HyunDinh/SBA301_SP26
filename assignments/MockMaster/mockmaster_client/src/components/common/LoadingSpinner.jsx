import { RefreshCw } from "lucide-react";

export default function LoadingSpinner() {
  return (
    <div className="flex flex-col items-center justify-center py-20 opacity-50">
      <RefreshCw size={44} className="animate-spin mb-4 text-indigo-600" />
      <p className="font-black tracking-widest text-sm uppercase text-slate-500">
        Đang tải dữ liệu...
      </p>
    </div>
  );
}