export default function WorkspaceModal({ isOpen, onClose, isEdit, initialData, onSave }) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-3xl shadow-2xl max-w-lg w-full overflow-hidden">
        <div className="p-8 border-b bg-slate-50">
          <h3 className="text-2xl font-black text-slate-900 tracking-tight">
            {isEdit ? "Chỉnh sửa Workspace" : "Tạo Workspace mới"}
          </h3>
        </div>

        {/* Thêm key để React reset form khi mở các bản ghi khác nhau */}
        <form 
          key={initialData?.workspaceId || "new"} 
          onSubmit={onSave} 
          className="p-8 space-y-6"
        >
          <div>
            <label className="block text-xs font-black text-slate-500 uppercase tracking-wider mb-2">
              Tên Workspace
            </label>
            <input
              type="text"
              required
              className="w-full px-4 py-3 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-400 outline-none"
              defaultValue={initialData?.workspaceName || ""}
              name="workspaceName"
            />
          </div>

          <div>
            <label className="block text-xs font-black text-slate-500 uppercase tracking-wider mb-2">
              Mô tả
            </label>
            <textarea
              className="w-full px-4 py-3 border border-slate-200 rounded-xl focus:ring-2 focus:ring-indigo-400 outline-none min-h-[100px]"
              defaultValue={initialData?.description || ""}
              name="description"
            />
          </div>

          <div className="flex gap-4 mt-8">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-3 border border-slate-300 rounded-xl text-slate-600 font-medium hover:bg-slate-50"
            >
              Hủy
            </button>
            <button
              type="submit"
              className="flex-1 py-3 bg-indigo-600 text-white rounded-xl font-medium hover:bg-indigo-700"
            >
              {isEdit ? "Cập nhật" : "Tạo mới"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}