import React, { useState } from 'react';
import { Container, Row, Col, Card, Button, Modal, Badge } from 'react-bootstrap';
import { OrchidsData as OrchidData } from '../data/ListOfOrchids';

function Orchids() {
  const [show, setShow] = useState(false);
  const [selectedOrchid, setSelectedOrchid] = useState(null);

  const handleClose = () => setShow(false);
  const handleShow = (orchid) => {
    setSelectedOrchid(orchid);
    setShow(true);
  };

  return (
    <Container className="mt-4">
      <Row>
        {OrchidData.map((orchid) => (
          <Col key={orchid.id} sm={12} md={6} lg={4} className="mb-4">
            <Card className="h-100 shadow-sm">
              <Card.Img variant="top" src={orchid.image} style={{ height: '250px', objectFit: 'cover' }} />
              <Card.Body className="d-flex flex-column">
                <Card.Title>{orchid.orchidName}</Card.Title>
                <Card.Text className="text-muted">{orchid.category}</Card.Text>
                <Button className="mt-auto" variant="primary" onClick={() => handleShow(orchid)}>
                  Detail
                </Button>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>

      <Modal show={show} onHide={handleClose} size="lg">
        <Modal.Header closeButton>
          {/* 1. Logic tô vàng Title nếu isSpecial là true */}
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
                <img 
                  src={selectedOrchid.image} 
                  alt={selectedOrchid.orchidName} 
                  className="img-fluid rounded shadow-sm mb-3" 
                />
              </Col>
              <Col md={7}>
                {/* 2. Hiển thị toàn bộ thông tin từ Dataset */}
                <h5 className="text-primary border-bottom pb-2">Orchid Information</h5>
                <ul className="list-unstyled">
                  <li className="mb-2"><strong>ID:</strong> {selectedOrchid.id}</li>
                  <li className="mb-2"><strong>Category:</strong> {selectedOrchid.category}</li>
                  <li className="mb-2">
                    <strong>Special Status:</strong> {selectedOrchid.isSpecial ? (
                      <Badge bg="danger">Special Orchid</Badge>
                    ) : (
                      <Badge bg="secondary">Normal</Badge>
                    )}
                  </li>
                  {/* Nếu dataset sau này có thêm origin hay color, bạn có thể thêm dòng tương tự dưới đây */}
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