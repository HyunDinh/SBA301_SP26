import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import useDashboardData from "../hooks/useDashboardData";
import useAdminData from "../hooks/useAdminData";

import Navbar from "../components/common/Navbar";
import DomainCard from "../components/common/DomainCard";
import MiniPostman from "../components/common/MiniPostman";
import WorkspaceModal from "../components/common/WorkspaceModal";
import UpgradeModal from "../components/common/UpgradeModal"; // Import thêm cái này
import WorkspaceCard from "../components/common/WorkspaceCard";
import LoadingSpinner from "../components/common/LoadingSpinner";

import { 
  Users, Database, Layout, TrendingUp, ShieldCheck, 
  Mail, Search, Plus, ChevronDown, ChevronUp, Folder, BarChart3,
  Lock, Unlock
} from "lucide-react";

export default function Dashboard() {
  const navigate = useNavigate();
  const {
    user, profile, workspaces, loading: dashLoading, resetting,
    searchTerm, setSearchTerm, copied, handleResetDomain,
    handleCopyDomain, testConfig, setTestConfig, testResult,
    setTestResult, isTesting, handleTestApi, isModalOpen,
    setIsModalOpen, currentWs, setCurrentWs, isEdit,
    setIsEdit, handleSaveWorkspace, handleDelete,
    isUpgradeModalOpen, setIsUpgradeModalOpen // Lấy thêm 2 biến này từ hook
  } = useDashboardData(navigate);

  const { stats, users, handleUpdatePlan, handleUpdateStatus, loading: adminLoading } = useAdminData();
  
  const [expandedUserId, setExpandedUserId] = useState(null);

  if (dashLoading || adminLoading || !user) return <LoadingSpinner />;

  // 1. GIAO DIỆN ADMIN
  if (profile?.role === "ADMIN") {
    return (
      <div className="min-h-screen bg-[#f1f5f9] text-slate-900 font-sans">
        <Navbar email={profile?.email} planName="ADMIN CONSOLE" />
        <main className="max-w-7xl mx-auto p-6 lg:p-10">
          <header className="mb-10 flex flex-col md:flex-row md:items-end justify-between gap-4">
            <div>
              <div className="flex items-center gap-2 text-indigo-600 mb-2 font-black uppercase tracking-[0.3em] text-[10px]">
                <ShieldCheck size={16} /> Hệ thống quản trị MockMaster
              </div>
              <h1 className="text-4xl font-black text-slate-900 italic uppercase tracking-tighter">
                Admin Overview
              </h1>
            </div>
          </header>

          <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
            <StatCard title="Người dùng" value={stats?.totalUsers} icon={<Users />} color="bg-indigo-600" />
            <StatCard title="Workspaces" value={stats?.totalWorkspaces} icon={<Layout />} color="bg-blue-600" />
            <StatCard title="Endpoints" value={stats?.totalEndpoints} icon={<Database />} color="bg-emerald-600" />
            <StatCard title="Doanh thu" value={`${stats?.totalRevenue?.toLocaleString()}đ`} icon={<TrendingUp />} color="bg-orange-500" />
          </section>

          <section>
            <h2 className="text-xl font-black text-slate-800 uppercase tracking-tight mb-6">User Management</h2>
            <div className="bg-white rounded-[2rem] border border-slate-200 overflow-hidden shadow-xl">
              <table className="w-full text-left">
                <thead className="bg-slate-50 border-b">
                  <tr>
                    <th className="p-6 text-xs font-black text-slate-400 uppercase tracking-widest">User Details</th>
                    <th className="p-6 text-xs font-black text-slate-400 uppercase tracking-widest">Resources</th>
                    <th className="p-6 text-xs font-black text-slate-400 uppercase tracking-widest">Plan & Status</th>
                    <th className="p-6 text-xs font-black text-slate-400 uppercase tracking-widest text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {users?.map((u) => (
                    <React.Fragment key={u.userId}>
                      <tr className={`hover:bg-indigo-50/40 transition-all cursor-pointer ${u.status === 'LOCKED' ? 'opacity-60 bg-slate-50' : ''}`}
                        onClick={() => setExpandedUserId(expandedUserId === u.userId ? null : u.userId)}>
                        <td className="p-6">
                          <div className="flex items-center gap-3">
                            <div className="w-10 h-10 rounded-xl flex items-center justify-center font-black bg-indigo-100 text-indigo-600">
                              {u.userId.charAt(0).toUpperCase()}
                            </div>
                            <div>
                              <div className="font-bold text-slate-800 flex items-center gap-2">
                                {u.userId} {u.status === 'LOCKED' && <Lock size={12} className="text-rose-500"/>}
                              </div>
                              <div className="text-xs text-slate-400"><Mail size={12} className="inline"/> {u.email}</div>
                            </div>
                          </div>
                        </td>
                        <td className="p-6 text-sm font-bold text-slate-600">{u.workspaceCount} Workspaces</td>
                        <td className="p-6">
                          <div className="flex flex-col gap-1">
                            <span className={`w-fit px-3 py-1 rounded-lg text-[9px] font-black border ${u.planName === 'PREMIUM' ? 'bg-amber-50 border-amber-100 text-amber-700' : 'bg-slate-50 border-slate-200 text-slate-600'}`}>
                              {u.planName}
                            </span>
                            <span className={`text-[9px] font-bold ${u.status === 'ACTIVE' ? 'text-emerald-500' : 'text-rose-500'}`}>● {u.status}</span>
                          </div>
                        </td>
                        <td className="p-6 text-right">
                          <div className="flex items-center justify-end gap-2" onClick={(e) => e.stopPropagation()}>
                            <button onClick={() => handleUpdateStatus(u.userId, u.status === 'ACTIVE' ? 'LOCKED' : 'ACTIVE')} className="p-2 rounded-lg">
                              {u.status === 'ACTIVE' ? <Unlock size={18} className="text-slate-400"/> : <Lock size={18} className="text-rose-600"/>}
                            </button>
                            <select className="bg-white border rounded-lg px-2 py-1 text-xs font-bold" value={u.planName} onChange={(e) => handleUpdatePlan(u.userId, e.target.value)}>
                              <option value="FREE">Gán FREE</option>
                              <option value="PREMIUM">Gán PREMIUM</option>
                            </select>
                          </div>
                        </td>
                      </tr>
                    </React.Fragment>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        </main>
      </div>
    );
  }

  // 2. GIAO DIỆN USER
  const filteredWorkspaces = workspaces.filter((ws) =>
    ws.workspaceName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-[#f8fafc] text-slate-900 font-sans">
      <Navbar email={profile?.email} planName={profile?.planName} />
      <main className="max-w-7xl mx-auto p-6 lg:p-10">
        <h1 className="text-3xl font-black text-slate-900 italic uppercase tracking-tighter mb-8">My Dashboard</h1>
        
        <DomainCard
          domain={profile?.systemDomain}
          baseUrl="http://localhost:8080/mock"
          copied={copied}
          onCopy={handleCopyDomain}
          onReset={handleResetDomain}
          resetting={resetting}
        />

        <MiniPostman
          config={testConfig} setConfig={setTestConfig}
          result={testResult} setResult={setTestResult}
          isTesting={isTesting} onTest={handleTestApi}
        />

        <div className="flex flex-col sm:flex-row gap-4 mb-8">
          <div className="relative flex-1 group">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 group-focus-within:text-indigo-500" size={18} />
            <input
              type="text"
              placeholder="Tìm kiếm dự án..."
              className="w-full pl-12 pr-4 py-3.5 bg-white border border-slate-200 rounded-2xl outline-none focus:border-indigo-500 shadow-sm"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <button
            onClick={() => { setIsEdit(false); setCurrentWs({ workspaceName: "", description: "" }); setIsModalOpen(true); }}
            className="bg-indigo-600 hover:bg-indigo-700 text-white py-4 px-8 rounded-2xl font-bold flex items-center justify-center gap-2 shadow-lg transition-all active:scale-95"
          >
            <Plus size={20} /> NEW WORKSPACE
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredWorkspaces.map((ws) => (
            <WorkspaceCard
              key={ws.workspaceId}
              workspace={ws}
              onClick={() => navigate(`/workspace/${ws.workspaceId}`)}
              onEdit={() => { setCurrentWs(ws); setIsEdit(true); setIsModalOpen(true); }}
              onDelete={(e) => handleDelete(e, ws.workspaceId)}
            />
          ))}
          {filteredWorkspaces.length === 0 && (
            <div className="col-span-full py-20 text-center border-2 border-dashed border-slate-200 rounded-[2.5rem]">
              <p className="text-slate-400 font-medium italic">Chưa có workspace nào.</p>
            </div>
          )}
        </div>
      </main>

      {/* MODAL TẠO WORKSPACE */}
      {isModalOpen && (
        <WorkspaceModal
          isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}
          isEdit={isEdit} initialData={currentWs} onSave={handleSaveWorkspace}
        />
      )}

      {/* MODAL NÂNG CẤP PREMIUM (Rất quan trọng) */}
      <UpgradeModal 
        isOpen={isUpgradeModalOpen} 
        onClose={() => setIsUpgradeModalOpen(false)} 
      />
    </div>
  );
}

function StatCard({ title, value, icon, color }) {
  return (
    <div className="bg-white p-6 rounded-[2rem] border border-slate-200 flex items-center gap-5 shadow-sm">
      <div className={`w-14 h-14 ${color} text-white rounded-2xl flex items-center justify-center shadow-lg`}>
        {icon}
      </div>
      <div>
        <p className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-1">{title}</p>
        <p className="text-2xl font-black text-slate-800">{value ?? "0"}</p>
      </div>
    </div>
  );
}