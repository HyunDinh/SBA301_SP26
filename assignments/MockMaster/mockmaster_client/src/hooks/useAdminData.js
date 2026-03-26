import { useState, useEffect, useCallback } from "react";
import api from "../services/api";

export default function useAdminData() {
  const [stats, setStats] = useState(null);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchAdminData = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get("/admin/stats");
      setStats(res.data);
      setUsers(res.data.recentUsers || []);
    } catch (err) {
      console.error("Lỗi lấy dữ liệu quản trị:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  const handleUpdatePlan = async (userId, planName) => {
    try {
      await api.patch(`/admin/users/${userId}/plan?planName=${planName}`);
      alert("Cập nhật Plan thành công!");
      fetchAdminData(); 
    } catch (err) {
      alert("Lỗi: " + (err.response?.data || "Không thể cập nhật plan"));
    }
  };

  // CHỨC NĂNG MỚI: Cập nhật Status (ACTIVE/LOCKED)
  const handleUpdateStatus = async (userId, status) => {
    try {
      await api.patch(`/admin/users/${userId}/status?status=${status}`);
      alert(`Đã chuyển trạng thái sang ${status}`);
      fetchAdminData();
    } catch (err) {
      alert("Lỗi: " + (err.response?.data || "Không thể cập nhật trạng thái"));
    }
  };

  useEffect(() => {
    fetchAdminData();
  }, [fetchAdminData]);

  return { 
    stats, 
    users, 
    loading, 
    handleUpdatePlan, 
    handleUpdateStatus, // Trả về thêm hàm này
    refresh: fetchAdminData 
  };
}