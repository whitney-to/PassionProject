import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>© 2023 Tourista. All rights reserved</p>
      </Col>
    </Row>
  </div>
);

export default Footer;
