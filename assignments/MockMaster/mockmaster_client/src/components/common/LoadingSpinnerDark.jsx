import { Loader2 } from 'lucide-react';

export default function LoadingSpinnerDark({ message = 'LOADING...' }) {
  return (
    <div className="h-screen bg-[#1e1e1e] flex flex-col items-center justify-center text-gray-500 font-mono">
      <Loader2 className="animate-spin mb-4" size={40} />
      <p className="animate-pulse">{message}</p>
    </div>
  );
}