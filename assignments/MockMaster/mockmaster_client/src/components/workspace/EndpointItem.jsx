import { X } from 'lucide-react';

export default function EndpointItem({
  endpoint,
  folderId,
  isSelected,
  onSelect,
  onDelete,
  getMethodColor,
}) {
  return (
    <div
      onClick={onSelect}
      className={`group/ep flex items-center justify-between pl-9 pr-3 py-1.5 text-[11px] cursor-pointer transition-all border-l-2 ${
        isSelected
          ? 'bg-[#37373d] border-blue-500 text-white'
          : 'border-transparent hover:bg-[#2a2d2e] text-gray-400'
      }`}
    >
      <div className="flex items-center gap-3 truncate">
        <span className={`font-black w-8 text-[9px] ${getMethodColor(endpoint.method)}`}>
          {endpoint.method}
        </span>
        <span className="truncate font-mono">{endpoint.path}</span>
      </div>
      <button
        onClick={(e) => onDelete(e, endpoint.endpointId)}
        className="opacity-0 group-hover/ep:opacity-100 p-1 hover:text-red-400 transition"
      >
        <X size={12} />
      </button>
    </div>
  );
}