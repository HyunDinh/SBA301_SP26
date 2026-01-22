import React, { useState } from 'react';
import { Container, Form, Button, Card, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext'; // Import hook

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  
  const navigate = useNavigate();
  const { dispatch } = useAuth(); // Lấy hàm dispatch từ context

  const handleLogin = (e) => {
    e.preventDefault();
    
    // Giả lập kiểm tra tài khoản
    if (username === 'admin' && password === '123456') {
      // Gửi action lên Reducer thông qua Context
      dispatch({ 
        type: 'LOGIN_SUCCESS', 
        payload: { name: 'Admin User', role: 'admin' } 
      });
      
      alert('Login Successful!');
      navigate('/');
    } else {
      setError('Invalid credentials! Hint: admin / 123456');
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '50vh' }}>
      <Card style={{ width: '380px' }} className="shadow-lg border-0 login-card">
        <Card.Body className="p-4">
          <h3 className="text-center mb-4">MEMBER LOGIN</h3>
          {error && <Alert variant="danger" className="py-2">{error}</Alert>}
          <Form onSubmit={handleLogin}>
            <Form.Group className="mb-3">
              <Form.Label>Username</Form.Label>
              <Form.Control type="text" onChange={(e) => setUsername(e.target.value)} required />
            </Form.Group>
            <Form.Group className="mb-4">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" onChange={(e) => setPassword(e.target.value)} required />
            </Form.Group>
            <div className="d-grid gap-2">
              <Button variant="primary" type="submit">Login Now</Button>
              <Button variant="light" onClick={() => navigate('/')}>Cancel</Button>
            </div>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
}
export default Login;