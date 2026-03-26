import { useState, useEffect } from "react";
import api from "../services/api";

export default function useWorkspaceDetail(workspaceId, navigate) {
  const [workspaceName, setWorkspaceName] = useState("");
  const [folders, setFolders] = useState([]);
  const [selectedEndpoint, setSelectedEndpoint] = useState(null);
  const [loading, setLoading] = useState(true);
  const [profile, setProfile] = useState(null);
  const [copied, setCopied] = useState(false);
  const [expandedFolders, setExpandedFolders] = useState({});

  const MOCK_BASE_URL = "http://localhost:8080/mock";

  useEffect(() => {
    fetchWorkspaceData();
    fetchUserProfile();
  }, [workspaceId]);

  const fetchWorkspaceData = async () => {
    try {
      const res = await api.get(`/workspaces/${workspaceId}/full`);
      setWorkspaceName(res.data.workspaceName);
      setFolders(res.data.folders || []);

      // Cập nhật expandedFolders một cách thông minh
      setExpandedFolders((prev) => {
        const nextExpanded = { ...prev };
        (res.data.folders || []).forEach((f) => {
          // Chỉ set bằng true nếu folderId này chưa từng tồn tại trong state trước đó
          if (nextExpanded[f.folderId] === undefined) {
            nextExpanded[f.folderId] = true;
          }
        });
        return nextExpanded;
      });

      setLoading(false);
    } catch (err) {
      console.error("Lỗi fetch workspace:", err);
      if (err.response?.status === 403) navigate("/login");
    }
  };

  const fetchUserProfile = async () => {
    try {
      const res = await api.get("/users/profile");
      setProfile(res.data);
    } catch (err) {
      console.error("Lỗi fetch profile:", err);
    }
  };

  const toggleFolder = (folderId) => {
    setExpandedFolders((prev) => ({
      ...prev,
      [folderId]: !prev[folderId],
    }));
  };

  const getFullUrl = () => {
    if (!selectedEndpoint || !profile) return "";
    const domain = profile.systemDomain || "default";
    const path = selectedEndpoint.path.startsWith("/")
      ? selectedEndpoint.path
      : `/${selectedEndpoint.path}`;
    return `${MOCK_BASE_URL}/${domain}/${workspaceId}${path}`;
  };

  const handleCopyFullUrl = () => {
    const url = getFullUrl();
    if (!url) return;
    navigator.clipboard.writeText(url);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleCreateFolder = async () => {
    const name = prompt("Nhập tên Folder mới:");
    if (!name) return;
    try {
      await api.post(`/folders/workspace/${workspaceId}`, { folderName: name });
      fetchWorkspaceData();
    } catch (err) {
      alert("Lỗi khi tạo folder");
    }
  };

  const handleEditFolder = async (folderId, oldName) => {
    const newName = prompt("Đổi tên folder:", oldName);
    if (!newName || newName === oldName) return;
    try {
      await api.put(`/folders/${folderId}`, { folderName: newName });
      fetchWorkspaceData();
    } catch (err) {
      alert("Lỗi khi sửa folder");
    }
  };

  const handleDeleteFolder = async (folderId) => {
    if (!window.confirm("Xóa folder sẽ xóa sạch API bên trong. Tiếp tục chứ?"))
      return;
    try {
      await api.delete(`/folders/${folderId}`);
      if (selectedEndpoint?.folderId === folderId) setSelectedEndpoint(null);
      fetchWorkspaceData();
    } catch (err) {
      alert("Lỗi khi xóa folder");
    }
  };

  const handleCreateEndpoint = async (folderId) => {
    try {
      const newEpData = {
        path: "/new-api",
        method: "GET",
        statusCode: 200,
        contentType: "application/json",
        responseBody:
          '{\n  "status": "success",\n  "message": "Hello Dinh Hung"\n}',
        delayMs: 0,
      };
      const res = await api.post(`/endpoints/folder/${folderId}`, newEpData);
      await fetchWorkspaceData();
      setSelectedEndpoint({ ...res.data, folderId });
    } catch (err) {
      alert("Lỗi khi tạo API");
    }
  };

  const handleSaveEndpoint = async () => {
    if (!selectedEndpoint) return;
    try {
      await api.put(
        `/endpoints/${selectedEndpoint.endpointId}`,
        selectedEndpoint,
      );
      alert("Đã lưu thay đổi!");
      fetchWorkspaceData();
    } catch (err) {
      alert("Lưu thất bại!");
    }
  };

  const handleDeleteEndpoint = async (e, epId) => {
    e.stopPropagation();
    if (!window.confirm("Xóa API này?")) return;
    try {
      await api.delete(`/endpoints/${epId}`);
      if (selectedEndpoint?.endpointId === epId) setSelectedEndpoint(null);
      fetchWorkspaceData();
    } catch (err) {
      alert("Lỗi khi xóa API");
    }
  };

  const getMethodColor = (method) => {
    const colors = {
      GET: "text-green-400",
      POST: "text-yellow-400",
      PUT: "text-blue-400",
      DELETE: "text-red-400",
      PATCH: "text-purple-400",
    };
    return colors[method] || "text-gray-400";
  };

  return {
    workspaceName,
    folders,
    selectedEndpoint,
    setSelectedEndpoint,
    profile,
    loading,
    copied,
    handleCopyFullUrl,
    handleSaveEndpoint,
    toggleFolder,
    expandedFolders,
    handleCreateFolder,
    handleCreateEndpoint,
    handleEditFolder,
    handleDeleteFolder,
    handleDeleteEndpoint,
    getMethodColor,
  };
}
