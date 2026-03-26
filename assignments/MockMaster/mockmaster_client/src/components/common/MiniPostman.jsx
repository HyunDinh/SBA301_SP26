import { Terminal, Play, RefreshCw, X, Code2 } from "lucide-react";

export default function MiniPostman({
  config,
  setConfig,
  result,
  setResult,
  isTesting,
  onTest,
}) {
  return (
    <div className="bg-[#1e293b] rounded-3xl p-6 sm:p-8 shadow-2xl border border-slate-700 mb-8">
      <div className="flex items-center gap-3 mb-6">
        <div className="w-9 h-9 bg-indigo-500/20 rounded-xl flex items-center justify-center">
          <Terminal size={18} className="text-indigo-400" />
        </div>
        <h2 className="text-sm font-black text-white uppercase tracking-[0.2em]">API Quick Tester</h2>
      </div>

      <div className="space-y-5">
        <div className="flex flex-col sm:flex-row gap-3">
          <select
            className="bg-slate-800 border border-slate-700 text-indigo-300 font-bold text-sm rounded-xl px-4 py-3 outline-none focus:border-indigo-500 min-w-[110px]"
            value={config.method}
            onChange={(e) => setConfig({ ...config, method: e.target.value })}
          >
            {["GET", "POST", "PUT", "PATCH", "DELETE"].map((m) => (
              <option key={m} value={m}>
                {m}
              </option>
            ))}
          </select>

          <input
            type="text"
            placeholder="https://your-mock-url..."
            className="flex-1 bg-slate-800 border border-slate-700 rounded-xl px-4 py-3 text-white text-sm font-mono outline-none focus:border-indigo-500"
            value={config.url}
            onChange={(e) => setConfig({ ...config, url: e.target.value })}
          />

          <button
            onClick={onTest}
            disabled={isTesting || !config.url.trim()}
            className="bg-indigo-600 hover:bg-indigo-500 disabled:opacity-50 text-white px-6 py-3 rounded-xl font-bold flex items-center justify-center gap-2 transition-all min-w-[110px]"
          >
            {isTesting ? <RefreshCw size={18} className="animate-spin" /> : <Play size={18} />}
            SEND
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 block">
              Headers (JSON)
            </label>
            <textarea
              className="w-full h-28 bg-slate-800/60 border border-slate-700 rounded-xl p-3 text-xs font-mono text-slate-200 outline-none focus:border-indigo-500 resize-none"
              value={config.headers}
              onChange={(e) => setConfig({ ...config, headers: e.target.value })}
              spellCheck={false}
            />
          </div>

          <div>
            <label className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 block">
              Body (JSON) {config.method === "GET" && "(không áp dụng)"}
            </label>
            <textarea
              disabled={config.method === "GET"}
              className={`w-full h-28 bg-slate-800/60 border border-slate-700 rounded-xl p-3 text-xs font-mono text-slate-200 outline-none focus:border-indigo-500 resize-none ${
                config.method === "GET" ? "opacity-40 cursor-not-allowed" : ""
              }`}
              value={config.body}
              onChange={(e) => setConfig({ ...config, body: e.target.value })}
              spellCheck={false}
            />
          </div>
        </div>

        {result && (
          <div className="mt-6 pt-6 border-t border-slate-700 animate-in fade-in duration-300">
            <div className="flex items-center justify-between mb-4">
              <div className="flex gap-4 items-center">
                <span
                  className={`text-xs font-black px-3 py-1.5 rounded-lg ${
                    result.error ? "bg-red-500/20 text-red-400" : "bg-green-500/20 text-green-400"
                  }`}
                >
                  {result.status}
                </span>
                <span className="text-xs text-slate-400 font-medium">{result.time} ms</span>
              </div>
              <button
                onClick={() => setResult(null)}
                className="text-slate-400 hover:text-slate-200 transition-colors"
              >
                <X size={18} />
              </button>
            </div>

            <div className="relative group">
              <pre className="bg-[#0f172a] p-5 rounded-2xl border border-slate-800 text-indigo-200 text-[13px] font-mono overflow-auto max-h-72 custom-scrollbar leading-relaxed shadow-inner">
                {typeof result.data === "object"
                  ? JSON.stringify(result.data, null, 2)
                  : String(result.data || "No content")}
              </pre>
              <div className="absolute right-3 top-3 opacity-0 group-hover:opacity-60 transition-opacity pointer-events-none">
                <Code2 size={16} className="text-slate-500" />
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}