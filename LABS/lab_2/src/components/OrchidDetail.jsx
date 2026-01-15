import React from 'react';
import { useParams, Link } from 'react-router-dom';
import { Container, Row, Col, Badge, Button, Card } from 'react-bootstrap';
import { OrchidsData } from '../data/ListOfOrchids';

function OrchidDetail() {
  const { id } = useParams();
  const orchid = OrchidsData.find(o => o.id === id);

  if (!orchid) {
    return (
      <Container className="mt-5 text-center">
        <div className="alert alert-danger p-5">
          <h2>404 - NOT FOUND</h2>
          <p>The orchid you are looking for does not exist.</p>
          <Link to="/"><Button variant="dark">Back to Store</Button></Link>
        </div>
      </Container>
    );
  }

  return (
    <Container className="mt-5">
      <Row className="bg-white p-4 rounded shadow-sm">
        <Col md={5}>
          <img src={`/${orchid.image}`} alt={orchid.orchidName} className="img-fluid detail-image" />
        </Col>
        <Col md={7}>
          <div className="d-flex align-items-center mb-3">
            <h1 className="me-3">{orchid.orchidName}</h1>
            {orchid.isSpecial && <Badge bg="danger">Special Edition</Badge>}
          </div>
          <Badge bg="secondary" className="mb-3">{orchid.category}</Badge>
          <hr />
          <p className="text-secondary" style={{ lineHeight: '1.8', textAlign: 'justify' }}>{orchid.description}</p>
          <Link to="/"><Button variant="outline-dark" className="mt-3">← Back to Gallery</Button></Link>
        </Col>
      </Row>
    </Container>
  );
}
export default OrchidDetail;