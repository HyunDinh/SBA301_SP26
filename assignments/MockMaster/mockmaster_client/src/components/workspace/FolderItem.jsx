import { ChevronDown, ChevronRight, Folder as FolderIcon, Plus, Edit3, Trash2 } from 'lucide-react';
import EndpointItem from './EndpointItem';

export default function FolderItem({
  folder,
  isExpanded,
  toggleFolder,
  selectedEndpoint,
  onCreateEndpoint,
  onEditFolder,
  onDeleteFolder,
  onSelectEndpoint,
  onDeleteEndpoint,
  getMethodColor,
}) {
  return (
    <div className="group/folder border-b border-gray-800/30">
      {/* Folder Header - Click để toggle */}
      <div
        className="flex items-center justify-between p-2.5 hover:bg-[#2a2d2e] cursor-pointer transition"
        onClick={toggleFolder}
      >
        <div className="flex items-center gap-2 overflow-hidden flex-1">
          {isExpanded ? (
            <ChevronDown size={14} className="text-gray-400" />
          ) : (
            <ChevronRight size={14} className="text-gray-600" />
          )}
          <FolderIcon size={16} className="text-yellow-500 shrink-0" />
          <span className="text-[11px] font-bold text-gray-300 truncate">
            {folder.folderName}
          </span>
        </div>

        {/* Action buttons chỉ hiện khi hover */}
        <div className="flex items-center gap-1 opacity-0 group-hover/folder:opacity-100 transition">
          <button
            onClick={(e) => {
              e.stopPropagation();
              onCreateEndpoint();
            }}
            className="p-1 hover:text-green-400"
            title="Add Endpoint"
          >
            <Plus size={14} />
          </button>
          <button
            onClick={(e) => {
              e.stopPropagation();
              onEditFolder();
            }}
            className="p-1 hover:text-white"
            title="Rename"
          >
            <Edit3 size={12} />
          </button>
          <button
            onClick={(e) => {
              e.stopPropagation();
              onDeleteFolder();
            }}
            className="p-1 hover:text-red-400"
            title="Delete Folder"
          >
            <Trash2 size={12} />
          </button>
        </div>
      </div>

      {/* Endpoints list - chỉ hiện khi folder mở */}
      {isExpanded && (
        <div className="bg-[#1e1e1e]/40 min-h-[40px] pt-1 pb-2">  {/* thêm min-h + padding để dễ nhìn */}
          {console.log('Rendering endpoints for folder:', folder.folderName, folder.endpoints)}
          {folder.endpoints?.map(ep => (
            <EndpointItem
              key={ep.endpointId}
              endpoint={ep}
              folderId={folder.folderId}
              isSelected={selectedEndpoint?.endpointId === ep.endpointId}
              onSelect={() => onSelectEndpoint({ ...ep, folderId: folder.folderId })}
              onDelete={onDeleteEndpoint}
              getMethodColor={getMethodColor}
            />
          ))}
        </div>
      )}
    </div>
  );
}