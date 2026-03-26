import { ArrowLeft, Plus } from 'lucide-react';
import FolderItem from './FolderItem';

export default function WorkspaceSidebar({
  workspaceName,
  folders,
  selectedEndpoint,
  expandedFolders,
  toggleFolder,
  onCreateFolder,
  onCreateEndpoint,
  onEditFolder,
  onDeleteFolder,
  onSelectEndpoint,
  onDeleteEndpoint,
  getMethodColor,
  navigate,
}) {
  return (
    <div className="w-72 border-r border-gray-800 flex flex-col bg-[#252526] select-none">
      <div className="p-4 border-b border-gray-800 flex items-center justify-between bg-[#2d2d2d]">
        <div className="flex items-center gap-2 overflow-hidden">
          <button onClick={() => navigate('/dashboard')} className="hover:text-white transition">
            <ArrowLeft size={18} />
          </button>
          <h1 className="font-bold text-white text-xs truncate uppercase tracking-tighter">
            {workspaceName}
          </h1>
        </div>
        <button
          onClick={onCreateFolder}
          className="p-1 hover:bg-gray-600 rounded text-blue-400"
          title="New Folder"
        >
          <Plus size={18} />
        </button>
      </div>

      <div className="flex-1 overflow-y-auto custom-scrollbar">
        {folders.map(folder => (
          <FolderItem
            key={folder.folderId}
            folder={folder}
            isExpanded={!!expandedFolders[folder.folderId]}
            toggleFolder={() => toggleFolder(folder.folderId)}
            selectedEndpoint={selectedEndpoint}
            onCreateEndpoint={() => onCreateEndpoint(folder.folderId)}
            onEditFolder={() => onEditFolder(folder.folderId, folder.folderName)}
            onDeleteFolder={() => onDeleteFolder(folder.folderId)}
            onSelectEndpoint={onSelectEndpoint}
            onDeleteEndpoint={onDeleteEndpoint}
            getMethodColor={getMethodColor}
          />
        ))}
      </div>
    </div>
  );
}