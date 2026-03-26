import { ShieldCheck, Copy, Check, RefreshCw } from "lucide-react";

export default function DomainCard({
  domain,
  baseUrl = "http://localhost:8080/mock",
  copied,
  onCopy,
  onReset,
  resetting,
}) {
  const fullDomain = domain ? `${baseUrl}/${domain}` : `${baseUrl}/...........`;

  return (
    <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 mb-8">
      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-2 mb-1">
          <ShieldCheck size={14} className="text-indigo-500" />
          <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest">
            Your Private System Domain
          </p>
        </div>
        <code className="text-indigo-600 font-bold text-sm bg-indigo-50 px-3 py-1.5 rounded block truncate">
          {fullDomain}
        </code>
      </div>
      <div className="flex gap-2 self-start sm:self-center">
        <button
          onClick={onCopy}
          className={`p-3 rounded-xl transition-all flex items-center gap-2 text-xs font-bold min-w-[88px] justify-center ${
            copied ? "bg-green-500 text-white" : "bg-slate-100 text-slate-600 hover:bg-indigo-600 hover:text-white"
          }`}
        >
          {copied ? <Check size={16} /> : <Copy size={16} />}
          {copied ? "COPIED" : "COPY"}
        </button>
        <button
          onClick={onReset}
          disabled={resetting}
          className="p-3 bg-slate-100 text-slate-600 rounded-xl hover:bg-red-50 hover:text-red-600 transition-all shadow-sm"
          title="Reset domain"
        >
          <RefreshCw size={16} className={resetting ? "animate-spin" : ""} />
        </button>
      </div>
    </div>
  );
}