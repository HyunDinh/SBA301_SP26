import { Copy, Check, Save } from 'lucide-react';

export default function EndpointUrlBar({
  endpoint,
  setEndpoint,
  profile,
  workspaceId,
  copied,
  onCopyFullUrl,
  onSave,
  getMethodColor,
}) {
  const domainPart = profile?.systemDomain || '...';

  return (
    <div className="p-4 bg-[#252526] border-b border-gray-800 flex items-center gap-3">
      <div className="flex flex-1 items-center bg-[#1e1e1e] border border-gray-700 rounded focus-within:border-blue-500 transition shadow-inner overflow-hidden">
        <select
          className={`bg-transparent px-4 py-2 text-[11px] font-black border-r border-gray-700 outline-none cursor-pointer ${getMethodColor(
            endpoint.method
          )}`}
          value={endpoint.method}
          onChange={(e) => setEndpoint({ ...endpoint, method: e.target.value })}
        >
          {['GET', 'POST', 'PUT', 'DELETE', 'PATCH'].map((m) => (
            <option key={m} value={m} className="bg-[#252526] text-white">
              {m}
            </option>
          ))}
        </select>

        <div className="px-3 text-gray-600 text-[11px] font-mono select-none truncate">
          {domainPart}/{workspaceId}
        </div>

        <input
          className="flex-1 bg-transparent py-2 text-[11px] text-blue-300 outline-none font-mono"
          value={endpoint.path}
          onChange={(e) => setEndpoint({ ...endpoint, path: e.target.value })}
        />
      </div>

      <button
        onClick={onCopyFullUrl}
        className={`px-4 py-2 rounded text-[11px] font-bold flex items-center gap-2 transition active:scale-95 border ${
          copied
            ? 'bg-green-600 border-green-600 text-white shadow-lg shadow-green-900/20'
            : 'bg-[#2d2d2d] border-gray-700 text-gray-300 hover:bg-gray-700'
        }`}
      >
        {copied ? <Check size={14} /> : <Copy size={14} />}
        {copied ? 'COPIED' : 'GET FULL URL'}
      </button>

      <button
        onClick={onSave}
        className="bg-blue-600 hover:bg-blue-500 text-white px-5 py-2 rounded text-[11px] font-bold flex items-center gap-2 transition active:scale-95 shadow-lg shadow-blue-900/20"
      >
        <Save size={14} /> SAVE API
      </button>
    </div>
  );
}