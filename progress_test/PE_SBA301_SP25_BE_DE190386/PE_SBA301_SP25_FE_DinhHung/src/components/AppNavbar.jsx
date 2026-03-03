import { useState } from 'react';
import { Navbar, Nav, NavDropdown, Container, Button, Modal, Form } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const AppNavbar = () => {
  const { user, login, logout } = useAuth();
  const [showLogin, setShowLogin] = useState(false);
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:8080/auth/login', credentials);
      login(res.data); 
      setShowLogin(false);
      setError('');
      navigate('/cars');
    } catch (err) {
      setError('Invalid email or password!');
    }
  };

  return (
    <>
      <Navbar bg="dark" variant="dark" expand="lg">
        <Container>
          {/* Yêu cầu: Student Code - Student Name PE Sping 25 */}
          <Navbar.Brand href="/">DE190386 - DinhHung PE Sping 25</Navbar.Brand>
          <Navbar.Toggle />
          <Navbar.Collapse>
            <Nav className="me-auto">
              <Nav.Link onClick={() => navigate('/')}>Home</Nav.Link>
              {/* Yêu cầu: NavDropdown = Car Management */}
              <NavDropdown title="Car Management">
                <NavDropdown.Item onClick={() => navigate('/cars')}>List all cars</NavDropdown.Item>
                <NavDropdown.Item onClick={() => navigate('/cars')}>Create a new car</NavDropdown.Item>
              </NavDropdown>
            </Nav>
            <Nav>
              {user ? (
                <Button variant="outline-danger" onClick={logout}>Logout</Button>
              ) : (
                <Button variant="outline-light" onClick={() => setShowLogin(true)}>Login</Button>
              )}
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>

      <Modal show={showLogin} onHide={() => setShowLogin(false)}>
        <Modal.Header closeButton>
          {/* Yêu cầu: Login to Cars Management System */}
          <Modal.Title>Login to Cars Management System</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {error && <div className="alert alert-danger">{error}</div>}
          <Form onSubmit={handleLogin}>
            <Form.Group className="mb-3">
              <Form.Label>Email Address</Form.Label>
              <Form.Control type="email" required onChange={e => setCredentials({...credentials, email: e.target.value})} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" required onChange={e => setCredentials({...credentials, password: e.target.value})} />
            </Form.Group>
            <Button variant="primary" type="submit" className="w-100">Login</Button>
          </Form>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default AppNavbar;