import React, { useState } from 'react';
import { Container, Row, Col, Card, Button, Modal, Badge, Form, InputGroup } from 'react-bootstrap';
import { OrchidsData as OrchidData } from '../data/ListOfOrchids';

function Orchids() {
  const [show, setShow] = useState(false);
  const [selectedOrchid, setSelectedOrchid] = useState(null);

  // --- 1. State cho Search, Filter, Sort ---
  const [searchTerm, setSearchTerm] = useState('');
  const [filterCategory, setFilterCategory] = useState('All');
  const [sortType, setSortType] = useState('default');

  const handleClose = () => setShow(false);
  const handleShow = (orchid) => {
    setSelectedOrchid(orchid);
    setShow(true);
  };

  // --- 2. Logic xử lý dữ liệu ---
  // Lấy danh sách các category duy nhất để đổ vào dropdown filter
  const categories = ['All', ...new Set(OrchidData.map(o => o.category))];

  const filteredOrchids = OrchidData
    .filter((orchid) => {
      const matchSearch = orchid.orchidName.toLowerCase().includes(searchTerm.toLowerCase());
      const matchCategory = filterCategory === 'All' || orchid.category === filterCategory;
      return matchSearch && matchCategory;
    })
    .sort((a, b) => {
      if (sortType === 'az') return a.orchidName.localeCompare(b.orchidName);
      if (sortType === 'za') return b.orchidName.localeCompare(a.orchidName);
      return 0; // default (theo ID)
    });

  return (
    <Container className="mt-4">
      {/* --- 3. Giao diện bộ lọc --- */}
      <Row className="mb-4 g-3">
        <Col md={4}>
          <Form.Control
            placeholder="Search orchids..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </Col>
        <Col md={4}>
          <Form.Select value={filterCategory} onChange={(e) => setFilterCategory(e.target.value)}>
            {categories.map(cat => <option key={cat} value={cat}>{cat}</option>)}
          </Form.Select>
        </Col>
        <Col md={4}>
          <Form.Select value={sortType} onChange={(e) => setSortType(e.target.value)}>
            <option value="default">Sort by: Default</option>
            <option value="az">Name: A to Z</option>
            <option value="za">Name: Z to A</option>
          </Form.Select>
        </Col>
      </Row>

      <hr />

      {/* --- 4. Hiển thị danh sách đã lọc/sắp xếp --- */}
      <Row>
        {filteredOrchids.length > 0 ? (
          filteredOrchids.map((orchid) => (
            <Col key={orchid.id} sm={12} md={6} lg={4} className="mb-4">
              <Card className="h-100 shadow-sm">
                <Card.Img variant="top" src={orchid.image} style={{ height: '250px', objectFit: 'cover' }} />
                <Card.Body className="d-flex flex-column">
                  <Card.Title>{orchid.orchidName}</Card.Title>
                  <Card.Text className="text-muted">
                    <Badge bg="info">{orchid.category}</Badge>
                  </Card.Text>
                  <Button className="mt-auto" variant="primary" onClick={() => handleShow(orchid)}>
                    Detail
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          ))
        ) : (
          <Col className="text-center py-5">
            <h4>No orchids found matching your criteria.</h4>
          </Col>
        )}
      </Row>

      {/* Modal giữ nguyên logic của bạn */}
      <Modal show={show} onHide={handleClose} size="lg">
        <Modal.Header closeButton>
          <Modal.Title 
            style={{ 
              backgroundColor: selectedOrchid?.isSpecial ? 'yellow' : 'transparent',
              padding: '5px 15px',
              borderRadius: '5px',
              display: 'inline-block'
            }}
          >
            {selectedOrchid?.orchidName}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedOrchid && (
            <Row>
              <Col md={5}>
                <img src={selectedOrchid.image} alt={selectedOrchid.orchidName} className="img-fluid rounded shadow-sm mb-3" />
              </Col>
              <Col md={7}>
                <h5 className="text-primary border-bottom pb-2">Orchid Information</h5>
                <ul className="list-unstyled">
                  <li className="mb-2"><strong>ID:</strong> {selectedOrchid.id}</li>
                  <li className="mb-2"><strong>Category:</strong> {selectedOrchid.category}</li>
                  <li className="mb-2">
                    <strong>Special Status:</strong> {selectedOrchid.isSpecial ? <Badge bg="danger">Special Orchid</Badge> : <Badge bg="secondary">Normal</Badge>}
                  </li>
                  {selectedOrchid.origin && <li className="mb-2"><strong>Origin:</strong> {selectedOrchid.origin}</li>}
                </ul>
                <hr />
                <h6>Description:</h6>
                <p style={{ textAlign: 'justify', fontSize: '0.95rem', color: '#555' }}>
                  {selectedOrchid.description}
                </p>
              </Col>
            </Row>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>Close</Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}

export default Orchids;