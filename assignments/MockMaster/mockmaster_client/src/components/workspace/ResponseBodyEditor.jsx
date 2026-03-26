export default function ResponseBodyEditor({ endpoint, setEndpoint }) {
  return (
    <div className="flex flex-col min-h-[500px]">
      <h3 className="text-[10px] font-bold text-gray-500 uppercase tracking-widest mb-4 border-b border-gray-800 pb-2">
        Response Body
      </h3>
      <textarea
        className="flex-1 bg-[#1a1a1a] border border-gray-700 rounded-lg p-4 font-mono text-[12px] text-blue-200 outline-none focus:border-blue-500 transition shadow-inner resize-none leading-relaxed"
        value={endpoint.responseBody}
        spellCheck={false}
        onChange={(e) => setEndpoint({ ...endpoint, responseBody: e.target.value })}
      />
    </div>
  );
}