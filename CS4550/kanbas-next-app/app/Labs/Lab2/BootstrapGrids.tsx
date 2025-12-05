"use client";

import { Col, Row, Container } from "react-bootstrap";

export default function BootstrapGrids() {
  return (
    <div>
      <div id="wd-bs-grid-system">
        <h2>Bootstrap</h2>
        <h2>Grid system</h2>

        <Container fluid className="mb-3">
          <Row>
            <Col className="bg-danger text-white">
              <h3>Left half</h3>
            </Col>
            <Col className="bg-primary text-white">
              <h3>Right half</h3>
            </Col>
          </Row>

          <Row className="mt-3">
            <Col xs={4} className="bg-warning">
              <h3>One third</h3>
            </Col>
            <Col xs={8} className="bg-success text-white">
              <h3>Two thirds</h3>
            </Col>
          </Row>

          <Row className="mt-3">
            <Col xs={2} className="bg-black text-white">
              <h3>Sidebar</h3>
            </Col>
            <Col xs={8} className="bg-secondary text-white">
              <h3>Main content</h3>
            </Col>
            <Col xs={2} className="bg-info">
              <h3>Sidebar</h3>
            </Col>
          </Row>
        </Container>
      </div>

      <div id="wd-bs-responsive-grids">
        <h2>Responsive grid system</h2>
        <Container fluid>
          <Row>
            <Col xs={12} md={6} xl={3} className="bg-warning"><h3>Column A</h3></Col>
            <Col xs={12} md={6} xl={3} className="bg-primary text-white"><h3>Column B</h3></Col>
            <Col xs={12} md={6} xl={3} className="bg-danger text-white"><h3>Column C</h3></Col>
            <Col xs={12} md={6} xl={3} className="bg-success text-white"><h3>Column D</h3></Col>
          </Row>
        </Container>
      </div>

      <div id="wd-bs-responsive-dramatic">
        <h2>Responsive grid system</h2>
        <Container fluid>
          <Row>
            {[...Array(12)].map((_, i) => (
              <Col
                key={i}
                xs={12} sm={6} md={4} lg={3} xl={2} xxl={1}
                className={["bg-warning","bg-primary text-white","bg-danger text-white","bg-success text-white"][i % 4]}
              >
                <h4>{i + 1}</h4>
              </Col>
            ))}
          </Row>
        </Container>
      </div>
    </div>
  );
}
