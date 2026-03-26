import { useState, useEffect } from "react";
import { getCurrentUser } from "../services/authService";
import api from "../services/api";
import axios from "axios";

export default function useDashboardData(navigate) {
  const user = getCurrentUser();

  const [profile, setProfile] = useState(null);
  const [workspaces, setWorkspaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [resetting, setResetting] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [copied, setCopied] = useState(false);
  
  // Trạng thái modal Premium
  const [isUpgradeModalOpen, setIsUpgradeModalOpen] = useState(false);

  // Mini Postman
  const [testConfig, setTestConfig] = useState({
    method: "GET",
    url: "",
    headers: '{\n  "Content-Type": "application/json"\n}',
    body: '{\n  "name": "Example",\n  "status": "testing"\n}',
  });
  const [testResult, setTestResult] = useState(null);
  const [isTesting, setIsTesting] = useState(false);

  // Modal Workspace
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentWs, setCurrentWs] = useState({ workspaceName: "", description: "" });
  const [isEdit, setIsEdit] = useState(false);

  const MOCK_BASE_URL = "http://localhost:8080/mock";

  useEffect(() => {
    if (!user) { navigate("/login"); return; }
    initDashboard();
  }, [navigate]);

  const initDashboard = async () => {
    setLoading(true);
    try {
      const [profileRes, wsRes] = await Promise.all([
        api.get("/users/profile"),
        api.get("/workspaces"),
      ]);
      setProfile(profileRes.data);
      setWorkspaces(wsRes.data);
    } catch (err) {
      if (err.response?.status === 403) navigate("/login");
    } finally {
      setLoading(false);
    }
  };

  const handleResetDomain = async () => {
    if (!window.confirm("Reset domain sẽ làm tất cả URL cũ không còn hoạt động. Xác nhận?")) return;
    setResetting(true);
    try {
      const res = await api.post("/users/reset-domain");
      setProfile((prev) => ({ ...prev, systemDomain: res.data.systemDomain }));
      alert("Đã cấp domain mới thành công!");
    } catch (err) {
      alert("Reset domain thất bại: " + (err.response?.data?.message || err.message));
    } finally {
      setResetting(false);
    }
  };

  const handleCopyDomain = () => {
    if (!profile?.systemDomain) return;
    const full = `${MOCK_BASE_URL}/${profile.systemDomain}`;
    navigator.clipboard.writeText(full);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleTestApi = async () => {
    if (!testConfig.url.trim()) return;
    setIsTesting(true);
    setTestResult(null);
    const start = Date.now();
    try {
      let headers = JSON.parse(testConfig.headers || "{}");
      let body = (testConfig.method !== "GET" && testConfig.method !== "DELETE") 
                 ? JSON.parse(testConfig.body || "{}") : null;

      const res = await axios({
        method: testConfig.method,
        url: testConfig.url.trim(),
        headers,
        data: body,
        timeout: 8000,
      });
      setTestResult({ status: res.status, time: Date.now() - start, data: res.data, headers: res.headers, error: false });
    } catch (err) {
      setTestResult({ status: err.response?.status || "ERROR", time: Date.now() - start, data: err.response?.data || err.message, error: true });
    } finally {
      setIsTesting(false);
    }
  };

  const handleSaveWorkspace = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const dataToSave = {
      ...currentWs,
      workspaceName: formData.get("workspaceName"),
      description: formData.get("description"),
    };

    try {
      if (isEdit) await api.put(`/workspaces/${currentWs.workspaceId}`, dataToSave);
      else await api.post("/workspaces", dataToSave);
      
      setIsModalOpen(false);
      setCurrentWs({ workspaceName: "", description: "" });
      initDashboard();
    } catch (err) {
      const msg = err.response?.data?.message || "";
      // PHÁT HIỆN LỖI GIỚI HẠN GÓI CƯỚC TỪ BACKEND
      if (msg.includes("PAYMENT_REQUIRED") || msg.includes("LIMIT_REACHED")) {
        setIsModalOpen(false); // Đóng modal hiện tại
        setIsUpgradeModalOpen(true); // Mở modal nâng cấp
      } else {
        alert("Lỗi: " + msg);
      }
    }
  };

  const handleDelete = async (e, id) => {
    e.stopPropagation();
    if (!window.confirm("Xóa workspace này?")) return;
    try {
      await api.delete(`/workspaces/${id}`);
      setWorkspaces(workspaces.filter((w) => w.workspaceId !== id));
    } catch (err) { alert("Xóa thất bại"); }
  };

  return {
    user, profile, workspaces, loading, resetting, searchTerm, setSearchTerm,
    copied, handleTestApi, handleResetDomain, handleCopyDomain,
    testConfig, setTestConfig, testResult, setTestResult, isTesting,
    isModalOpen, setIsModalOpen, currentWs, setCurrentWs, isEdit, setIsEdit,
    handleSaveWorkspace, handleDelete,
    isUpgradeModalOpen, setIsUpgradeModalOpen // Trả về các biến quản lý modal nâng cấp
  };
}