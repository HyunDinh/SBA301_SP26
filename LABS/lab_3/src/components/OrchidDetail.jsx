import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Container, Row, Col, Badge, Button, Spinner } from 'react-bootstrap';
import axiosClient from '../api/axiosClient';

function OrchidDetail() {
  const { id } = useParams();
  const [orchid, setOrchid] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosClient.get(`/orchids/${id}`)
      .then(res => {
        setOrchid(res.data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err);
        setLoading(false);
      });
  }, [id]);

  if (loading) return <Container className="text-center mt-5"><Spinner animation="border" /></Container>;
  if (!orchid) return <Container className="text-center mt-5"><h2>404 - Not Found</h2></Container>;

  return (
    <Container className="mt-5">
      <Row className="bg-white p-4 rounded shadow-sm">
        <Col md={5}>
          <img src={orchid.image.startsWith('http') ? orchid.image : `/${orchid.image}`} 
               alt={orchid.orchidName} className="img-fluid detail-image" />
        </Col>
        <Col md={7}>
          <h1>{orchid.orchidName} {orchid.isSpecial && <Badge bg="danger">Special</Badge>}</h1>
          <Badge bg="secondary" className="mb-3">{orchid.category}</Badge>
          <p className="text-secondary" style={{ textAlign: 'justify' }}>{orchid.description}</p>
          <Link to="/"><Button variant="outline-dark">← Back to Gallery</Button></Link>
        </Col>
      </Row>
    </Container>
  );
}
export default OrchidDetail;