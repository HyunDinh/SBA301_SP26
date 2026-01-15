import React, { useState, useMemo } from 'react';
import { Container, Row, Col, Card, Button, Badge, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { OrchidsData } from '../data/ListOfOrchids';

function Orchids() {
  const [searchTerm, setSearchTerm] = useState('');
  const [filterCategory, setFilterCategory] = useState('All');
  const [sortType, setSortType] = useState('default');

  const categories = useMemo(() => ['All', ...new Set(OrchidsData.map(o => o.category))], []);

  const filteredOrchids = useMemo(() => {
    let result = OrchidsData.filter((orchid) => {
      const matchSearch = orchid.orchidName.toLowerCase().includes(searchTerm.toLowerCase());
      const matchCategory = filterCategory === 'All' || orchid.category === filterCategory;
      return matchSearch && matchCategory;
    });

    if (sortType === 'az') result.sort((a, b) => a.orchidName.localeCompare(b.orchidName));
    if (sortType === 'za') result.sort((a, b) => b.orchidName.localeCompare(a.orchidName));
    
    return result;
  }, [searchTerm, filterCategory, sortType]);

  return (
    <Container className="mt-4">
      <Row className="mb-4 g-3">
        <Col md={4}><Form.Control placeholder="Search..." onChange={(e) => setSearchTerm(e.target.value)} /></Col>
        <Col md={4}>
          <Form.Select onChange={(e) => setFilterCategory(e.target.value)}>
            {categories.map(cat => <option key={cat} value={cat}>{cat}</option>)}
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
              <Card className="h-100 shadow-sm">
                <Card.Img variant="top" src={orchid.image} style={{ height: '220px', objectFit: 'cover' }} />
                <Card.Body className="d-flex flex-column">
                  <Card.Title>{orchid.orchidName}</Card.Title>
                  <Badge bg="info" className="mb-3 w-fit" style={{width: 'fit-content'}}>{orchid.category}</Badge>
                  <Link to={`/detail/${orchid.id}`} className="mt-auto">
                    <Button variant="primary" className="w-100">View Detail</Button>
                  </Link>
                </Card.Body>
              </Card>
            </Col>
          ))
        ) : (
          <Col className="text-center py-5"><div className="alert alert-warning">No orchids found for "{searchTerm}"</div></Col>
        )}
      </Row>
    </Container>
  );
}
export default Orchids;