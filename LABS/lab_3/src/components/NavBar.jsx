import { Navbar, Container, Nav, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext'; // 1. Import hook

function NavBar() {
  // 2. Lấy dữ liệu từ Context
  const { state, dispatch } = useAuth();

  return (
    <Navbar bg="dark" variant="dark" expand="lg" sticky="top" className="py-3 shadow">
      <Container>
        <Navbar.Brand as={Link} to="/" className="fw-bold">ORCHID STORE</Navbar.Brand>
        <Navbar.Toggle aria-controls="nav-menu" />
        <Navbar.Collapse id="nav-menu">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/">Home</Nav.Link>
            <Nav.Link as={Link} to="/about">About Us</Nav.Link>
            <Nav.Link as={Link} to="/contact">Contact</Nav.Link>
          </Nav>
          
          <Nav className="align-items-center">
            {/* 3. Kiểm tra trạng thái để hiển thị nút tương ứng */}
            {state.isAuthenticated ? (
              <>
                <span className="text-info me-3">Hi, {state.user.name}</span>
                <Button 
                  variant="outline-danger" 
                  size="sm" 
                  onClick={() => dispatch({ type: 'LOGOUT' })}
                >
                  Logout
                </Button>
              </>
            ) : (
              <Link to="/login">
                <Button variant="outline-info" size="sm">Login</Button>
              </Link>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default NavBar;