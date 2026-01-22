import React, { useState, useMemo, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Badge, Form, Spinner, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import axiosClient from '../api/axiosClient'; // Đảm bảo bạn đã tạo file này ở bước 4.2

function Orchids() {
  // State quản lý dữ liệu từ API
  const [orchids, setOrchids] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // State filter và search
  const [searchTerm, setSearchTerm] = useState('');
  const [filterCategory, setFilterCategory] = useState('All');
  const [sortType, setSortType] = useState('default');

  // 1. Fetch dữ liệu khi component mount
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        // Gọi API lấy danh sách orchids
        const response = await axiosClient.get('/orchids');
        setOrchids(response.data);
        setLoading(false);
      } catch (err) {
        console.error("Fetch error:", err);
        setError("Could not load data from server.");
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  // 2. Xử lý danh mục (Categories) động dựa trên dữ liệu từ API
  const categories = useMemo(() => {
    return ['All', ...new Set(orchids.map(o => o.category))];
  }, [orchids]);

  // 3. Logic Filter và Sort
  const filteredOrchids = useMemo(() => {
    let result = [...orchids].filter((orchid) => {
      const matchSearch = orchid.orchidName.toLowerCase().includes(searchTerm.toLowerCase());
      const matchCategory = filterCategory === 'All' || orchid.category === filterCategory;
      return matchSearch && matchCategory;
    });

    if (sortType === 'az') result.sort((a, b) => a.orchidName.localeCompare(b.orchidName));
    if (sortType === 'za') result.sort((a, b) => b.orchidName.localeCompare(a.orchidName));
    
    return result;
  }, [searchTerm, filterCategory, sortType, orchids]);

  // Hiển thị trạng thái Loading
  if (loading) return (
    <Container className="text-center mt-5">
      <Spinner animation="border" variant="primary" />
      <p>Loading Orchids...</p>
    </Container>
  );

  // Hiển thị trạng thái Lỗi
  if (error) return (
    <Container className="mt-5">
      <Alert variant="danger">{error}</Alert>
    </Container>
  );

  return (
    <Container className="mt-4">
      <Row className="mb-4 g-3">
        <Col md={4}>
          <Form.Control 
            placeholder="Search by name..." 
            onChange={(e) => setSearchTerm(e.target.value)} 
          />
        </Col>
        <Col md={4}>
          <Form.Select onChange={(e) => setFilterCategory(e.target.value)}>
            {categories.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </Form.Select>
        </Col>
        <Col md={4}>
          <Form.Select onChange={(e) => setSortType(e.target.value)}>
            <option value="default">Default Sort</option>
            <option value="az">A to Z</option>
            <option value="za">Z to A</option>
          </Form.Select>
        </Col>
      </Row>

      <Row>
        {filteredOrchids.length > 0 ? (
          filteredOrchids.map((orchid) => (
            <Col key={orchid.id} xs={12} sm={6} lg={3} className="mb-4">
              <Card className="h-100 shadow-sm card-hover">
                <Card.Img 
                  variant="top" 
                  src={orchid.image.startsWith('http') ? orchid.image : `/${orchid.image}`} 
                  style={{ height: '220px', objectFit: 'cover' }} 
                />
                <Card.Body className="d-flex flex-column">
                  <Card.Title>{orchid.orchidName}</Card.Title>
                  <div className="mb-3">
                    <Badge bg="info" className="me-2">{orchid.category}</Badge>
                    {orchid.isSpecial && <Badge bg="danger">Special</Badge>}
                  </div>
                  <Link to={`/detail/${orchid.id}`} className="mt-auto">
                    <Button variant="outline-primary" className="w-100">View Detail</Button>
                  </Link>
                </Card.Body>
              </Card>
            </Col>
          ))
        ) : (
          <Col className="text-center py-5">
            <Alert variant="warning">No orchids found!</Alert>
          </Col>
        )}
      </Row>
    </Container>
  );
}

export default Orchids;