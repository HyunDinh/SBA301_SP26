import { useState, useEffect } from 'react';
import { Container, Table, Button, Form, Card, Row, Col } from 'react-bootstrap';
import axiosClient from '../api/axiosClient';
import { useAuth } from '../context/AuthContext';

const CarManagement = () => {
  const { user } = useAuth();
  const [cars, setCars] = useState([]);
  const [countries, setCountries] = useState([]);
  
  // State quản lý form - Quan trọng: carId phải được khởi tạo để dùng cho PUT
  const [formData, setFormData] = useState({ 
    carId: '', 
    carName: '', 
    unitsInStock: 5, 
    unitPrice: 0, 
    countryId: '' 
  });
  const [isEditing, setIsEditing] = useState(false);

  // Kiểm tra quyền Admin dựa trên user.role (đã khớp với AuthController)
  const isAdmin = user && String(user.role) === '1';

  useEffect(() => {
    if (user) fetchData();
  }, [user]);

  const fetchData = async () => {
    try {
      const carRes = await axiosClient.get('/cars');
      setCars(carRes.data);
      const countryRes = await axiosClient.get('/countries');
      setCountries(countryRes.data);
    } catch (err) {
      console.error("Lỗi tải dữ liệu:", err);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm(`Bạn có chắc muốn xóa xe ID: ${id}?`)) {
      try {
        await axiosClient.delete(`/cars/${id}`);
        alert("Xóa thành công!");
        fetchData();
      } catch (err) {
        alert("Lỗi: " + (err.response?.data || "Không có quyền xóa"));
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Chuẩn bị dữ liệu gửi lên Backend
    const payload = {
      carName: formData.carName,
      unitsInStock: parseInt(formData.unitsInStock),
      unitPrice: parseFloat(formData.unitPrice),
      country: { 
        countryId: parseInt(formData.countryId) 
      }
    };

    try {
      if (isEditing) {
        // Gửi PUT kèm ID trên URL: /cars/{id}
        await axiosClient.put(`/cars/${formData.carId}`, payload);
        alert("Cập nhật thành công!");
      } else {
        // Gửi POST để thêm mới
        await axiosClient.post('/cars', payload);
        alert("Thêm mới thành công!");
      }
      
      handleReset();
      fetchData(); 
    } catch (err) {
      console.error("Lỗi chi tiết:", err.response?.data);
      alert("Lỗi: " + (err.response?.data || "Thao tác thất bại"));
    }
  };

  const handleReset = () => {
    setFormData({ carId: '', carName: '', unitsInStock: 5, unitPrice: 0, countryId: '' });
    setIsEditing(false);
  };

  if (!user) return <Container className="mt-5 text-center"><h3>Vui lòng đăng nhập.</h3></Container>;

  return (
    <Container className="mt-4">
      <Row>
        {/* PHẦN THÊM/SỬA - CHỈ ADMIN MỚI THẤY */}
        {isAdmin && (
          <Col md={4}>
            <Card className="p-3 shadow-sm border-primary mb-4">
              <Card.Header className="bg-primary text-white mb-3">
                <h5 className="mb-0">{isEditing ? `Sửa Xe (ID: ${formData.carId})` : "Thêm Xe Mới"}</h5>
              </Card.Header>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-2">
                  <Form.Label>Tên xe (Trên 10 ký tự)</Form.Label>
                  <Form.Control 
                    required minLength={11}
                    value={formData.carName} 
                    onChange={e => setFormData({...formData, carName: e.target.value})} 
                  />
                </Form.Group>

                <Form.Group className="mb-2">
                  <Form.Label>Số lượng tồn (5 - 20)</Form.Label>
                  <Form.Control 
                    type="number" required min={5} max={20}
                    value={formData.unitsInStock} 
                    onChange={e => setFormData({...formData, unitsInStock: e.target.value})} 
                  />
                </Form.Group>

                <Form.Group className="mb-2">
                  <Form.Label>Giá bán</Form.Label>
                  <Form.Control 
                    type="number" required
                    value={formData.unitPrice} 
                    onChange={e => setFormData({...formData, unitPrice: e.target.value})} 
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Quốc gia</Form.Label>
                  <Form.Select 
                    required value={formData.countryId} 
                    onChange={e => setFormData({...formData, countryId: e.target.value})}
                  >
                    <option value="">-- Chọn quốc gia --</option>
                    {countries.map(c => (
                      <option key={c.countryId} value={c.countryId}>{c.countryName}</option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <div className="d-grid gap-2">
                  <Button variant={isEditing ? "warning" : "success"} type="submit">
                    {isEditing ? "Lưu Cập Nhật" : "Thêm mới"}
                  </Button>
                  {isEditing && (
                    <Button variant="secondary" onClick={handleReset}>Hủy sửa</Button>
                  )}
                </div>
              </Form>
            </Card>
          </Col>
        )}

        {/* DANH SÁCH XE - TỰ ĐỘNG GIÃN RỘNG NẾU KHÔNG PHẢI ADMIN */}
        <Col md={isAdmin ? 8 : 12}>
          <Card className="p-3 shadow-sm">
            <h5 className="mb-3">Danh Sách Xe Hệ Thống</h5>
            <Table striped bordered hover responsive className="text-center align-middle">
              <thead className="table-dark">
                <tr>
                  <th>ID</th>
                  <th>Tên Xe</th>
                  <th>Tồn Kho</th>
                  <th>Quốc Gia</th>
                  {isAdmin && <th>Hành Động</th>}
                </tr>
              </thead>
              <tbody>
                {cars.map((car) => (
                  <tr key={car.carId}>
                    <td>{car.carId}</td>
                    <td className="text-start">{car.carName}</td>
                    <td>{car.unitsInStock}</td>
                    <td>{car.countryName || car.country?.countryName}</td>
                    {isAdmin && (
                      <td>
                        <Button variant="info" size="sm" className="me-2" onClick={() => {
                          // Gán dữ liệu vào form để sửa
                          setFormData({ 
                            carId: car.carId, 
                            carName: car.carName, 
                            unitsInStock: car.unitsInStock,
                            unitPrice: car.unitPrice,
                            // Kiểm tra lấy ID quốc gia dù data trả về kiểu gì
                            countryId: car.country?.countryId || car.countryId || '' 
                          });
                          setIsEditing(true);
                        }}>Sửa</Button>
                        <Button variant="danger" size="sm" onClick={() => handleDelete(car.carId)}>Xóa</Button>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </Table>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default CarManagement;