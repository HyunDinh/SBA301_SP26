export default function ResponseSettings({ endpoint, setEndpoint }) {
  return (
    <section>
      <h3 className="text-[10px] font-bold text-gray-500 uppercase tracking-widest mb-4 border-b border-gray-800 pb-2">
        Response Settings
      </h3>
      <div className="space-y-5">
        <div>
          <label className="text-[10px] text-gray-500 block mb-1.5 uppercase">HTTP Status Code</label>
          <input
            type="number"
            className="w-full bg-[#252526] border border-gray-700 rounded p-2 text-xs text-green-400 focus:border-blue-500 outline-none font-mono"
            value={endpoint.statusCode}
            onChange={(e) => setEndpoint({ ...endpoint, statusCode: parseInt(e.target.value) || 200 })}
          />
        </div>

        <div>
          <label className="text-[10px] text-gray-500 block mb-1.5 uppercase">Content-Type</label>
          <select
            className="w-full bg-[#252526] border border-gray-700 rounded p-2 text-xs text-white focus:border-blue-500 outline-none"
            value={endpoint.contentType}
            onChange={(e) => setEndpoint({ ...endpoint, contentType: e.target.value })}
          >
            <option value="application/json">application/json</option>
            <option value="text/html">text/html</option>
            <option value="application/xml">application/xml</option>
            <option value="text/plain">text/plain</option>
          </select>
        </div>

        <div>
          <label className="text-[10px] text-gray-500 block mb-1.5 uppercase">Network Latency (ms)</label>
          <input
            type="number"
            className="w-full bg-[#252526] border border-gray-700 rounded p-2 text-xs text-white focus:border-blue-500 outline-none font-mono"
            value={endpoint.delayMs || 0}
            onChange={(e) => setEndpoint({ ...endpoint, delayMs: parseInt(e.target.value) || 0 })}
          />
        </div>
      </div>
    </section>
  );
}