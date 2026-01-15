import React from 'react';
import { Container, Row, Col, Card, Accordion } from 'react-bootstrap';

function About() {
  return (
    <Container className="mt-5">
      {/* Phần giới thiệu chung */}
      <Row className="mb-5 align-items-center">
        <Col md={6}>
          <h2 className="display-4 fw-bold text-primary">About Orchid Store</h2>
          <p className="lead text-secondary">
            Được thành lập từ năm 2024, Orchid Store không chỉ là một cửa hàng bán hoa, 
            mà là nơi kết nối những người yêu thiên nhiên và cái đẹp.
          </p>
          <p style={{ textAlign: 'justify' }}>
            Chúng tôi chuyên cung cấp các dòng lan quý hiếm từ Dendrobium, Oncidium đến các dòng lan đột biến 
            được chăm sóc thủ công tại nhà vườn chuyên nghiệp. Mỗi nhành hoa trao đi là một sứ mệnh mang cái đẹp 
            đến cho không gian sống của khách hàng.
          </p>
        </Col>
        <Col md={6}>
          <img 
            src="https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?q=80&w=800" 
            alt="Garden" 
            className="img-fluid rounded shadow-lg"
          />
        </Col>
      </Row>

      {/* Phần Giá trị cốt lõi */}
      <Row className="mb-5 text-center">
        <h3 className="mb-4">Our Core Values</h3>
        <Col md={4} className="mb-3">
          <Card className="h-100 border-0 shadow-sm p-3">
            <Card.Body>
              <div className="fs-1 mb-2">🌱</div>
              <Card.Title>Quality First</Card.Title>
              <Card.Text>Đảm bảo nguồn giống khỏe mạnh, không sâu bệnh.</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} className="mb-3">
          <Card className="h-100 border-0 shadow-sm p-3">
            <Card.Body>
              <div className="fs-1 mb-2">🚚</div>
              <Card.Title>Fast Delivery</Card.Title>
              <Card.Text>Vận chuyển an toàn, giữ nguyên vẹn vẻ đẹp của hoa.</Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} className="mb-3">
          <Card className="h-100 border-0 shadow-sm p-3">
            <Card.Body>
              <div className="fs-1 mb-2">🤝</div>
              <Card.Title>Professional Support</Card.Title>
              <Card.Text>Tư vấn kỹ thuật chăm sóc lan trọn đời.</Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Phần Câu hỏi thường gặp (FAQ) */}
      <Row className="mb-5">
        <Col>
          <h3 className="text-center mb-4">Frequently Asked Questions</h3>
          <Accordion defaultActiveKey="0 shadow-sm">
            <Accordion.Item eventKey="0">
              <Accordion.Header>Làm sao để đặt hàng trực tuyến?</Accordion.Header>
              <Accordion.Body>
                Bạn có thể xem chi tiết từng loại lan trong danh mục sản phẩm và nhấn nút liên hệ để chúng tôi tư vấn trực tiếp qua Zalo/Phone.
              </Accordion.Body>
            </Accordion.Item>
            <Accordion.Item eventKey="1">
              <Accordion.Header>Chính sách bảo hành hoa như thế nào?</Accordion.Header>
              <Accordion.Body>
                Chúng tôi cam kết đổi trả 1-1 nếu cây bị hư hỏng do quá trình vận chuyển trong vòng 24h kể từ khi nhận hàng.
              </Accordion.Body>
            </Accordion.Item>
          </Accordion>
        </Col>
      </Row>
    </Container>
  );
}

export default About;