import { LayoutGrid, Settings, Trash2, ExternalLink } from "lucide-react";

export default function WorkspaceCard({ workspace, onClick, onEdit, onDelete }) {
  return (
    <div
      onClick={onClick}
      className="group bg-white rounded-3xl border border-slate-200 p-6 hover:shadow-xl hover:shadow-indigo-100/40 transition-all cursor-pointer flex flex-col h-64 border-b-4 border-b-transparent hover:border-b-indigo-500"
    >
      <div className="flex justify-between items-start mb-4">
        <div className="w-12 h-12 bg-slate-50 text-slate-400 rounded-2xl flex items-center justify-center group-hover:bg-indigo-600 group-hover:text-white transition-all">
          <LayoutGrid size={24} />
        </div>
        <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
          <button
            onClick={(e) => {
              e.stopPropagation();
              onEdit();
            }}
            className="p-2 hover:bg-indigo-50 rounded-lg text-slate-400 hover:text-indigo-600"
          >
            <Settings size={18} />
          </button>
          <button
            onClick={(e) => {
              e.stopPropagation();
              onDelete(e);
            }}
            className="p-2 hover:bg-red-50 rounded-lg text-slate-400 hover:text-red-600"
          >
            <Trash2 size={18} />
          </button>
        </div>
      </div>

      <h3 className="text-lg font-bold text-slate-800 group-hover:text-indigo-600 transition-colors mb-2 truncate">
        {workspace.workspaceName}
      </h3>

      <p className="text-slate-500 text-xs line-clamp-3 leading-relaxed mb-auto">
        {workspace.description || "Chưa có mô tả cho workspace này."}
      </p>

      <div className="pt-4 border-t border-slate-100 flex items-center justify-between mt-3">
        <span className="text-[10px] font-black text-slate-300 uppercase tracking-wider">
          {workspace.folderCount || 0} Folders
        </span>
        <div className="flex items-center text-indigo-600 font-bold text-xs gap-1 group-hover:gap-2 transition-all">
          OPEN <ExternalLink size={14} />
        </div>
      </div>
    </div>
  );
}