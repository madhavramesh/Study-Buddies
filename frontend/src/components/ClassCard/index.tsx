import React, { useState, useEffect } from 'react';
import { Button, Card, Col, Form, Modal } from 'react-bootstrap';
import './ClassCard.scss';
import axios from 'axios';

type ClassProps = {
  cid: number;
  name: string;
  number: string;
  desc: string;
  term: string;
  owner: any;
};

function emojifyTerm(term: string): string {
  const lcTerm = term.toLowerCase();
  if (lcTerm.includes('winter')) {
    return `${term} ☃️️`;
  }
  if (lcTerm.includes('spring')) {
    return `${term} 🌺`;
  }
  if (lcTerm.includes('summer')) {
    return `${term} ☀️`;
  }
  return `${term} 🍁`;
}

const ClassCard: React.FC<ClassProps> = ({ cid, name, number, desc, term, owner }) => {
  const [show, setShow] = useState(false);
  const [code, setCode] = useState('');

  const [validCode, setValidCode] = useState(false);
  const [validCodeMessage, setValidCodeMessage] = useState('');

  const checkJoinCode = async () => {
    const postParameters = {
      id: localStorage.getItem('user_id'),
      class_id: cid,
      class_code: code,
    };

    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };
    const response = await axios.post('http://localhost:4567/join_class', postParameters, config);
    console.log(response.data);
    if (response.data.status === 0) {
      setValidCode(true);
    }
    setValidCodeMessage(response.data.message);
  };

  useEffect(() => {
    const initializeValid = async () => {
      const postParameters = {
        id: localStorage.getItem('user_id'),
        class_id: cid,
        class_code: code,
      };

      const config = {
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
        },
      };
      const response = await axios.post('http://localhost:4567/join_class', postParameters, config);
      if (response.data.message === 'Already joined class!') {
        setValidCode(true);
        setValidCodeMessage(response.data.message);
      }
    };
    initializeValid();
  }, []);

  useEffect(() => {
    if (show === false && validCodeMessage !== 'Already joined class!') {
      setValidCodeMessage('');
    }
  }, [show]);

  return (
    <div className="class-card-container">
      <Card style={{ width: '18rem', height: '11rem' }} className="class-card">
        <Card.Body className="class-card-header">
          <Card.Title>{name}</Card.Title>
          <Card.Subtitle className="mb-2 text-muted">{emojifyTerm(term)}</Card.Subtitle>
          <Card.Text style={{ fontSize: '16px' }}>{owner ? '🥶 Owner 🥶' : ''}</Card.Text>
          <Button
            className="class-card-button"
            variant="outline-info"
            onClick={() => setShow(true)}
          >
            More Info
          </Button>
        </Card.Body>
      </Card>

      <Modal size="lg" show={show} onHide={() => setShow(false)} centered>
        <Modal.Header className="class-modal-header" closeButton>
          {number}
          {/* [{cid}] */}
        </Modal.Header>
        <Modal.Body className="class-modal-body">
          <p>
            <b>Class Name</b>: {name}
          </p>
          <p>
            <b>Class Term</b>: {term}
          </p>
          <p>
            <b>Class Description</b>: {desc}
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Form noValidate validated={validCode}>
            <Form.Row className="align-items-center">
              <Col xs="auto">
                <Form.Label htmlFor="inlineFormInput" srOnly>
                  Join Class
                </Form.Label>
                <Form.Control
                  className="mb-2"
                  id="inlineFormInput"
                  placeholder="Enter Class Code..."
                  isInvalid={!validCode && validCodeMessage !== ''}
                  onChange={(e: any) => setCode(e.target.value)}
                />
                <Form.Control.Feedback type="invalid">{validCodeMessage}</Form.Control.Feedback>
                <Form.Control.Feedback>{validCodeMessage}</Form.Control.Feedback>
              </Col>
              <Col xs="auto">
                <Button variant="primary" className="mb-2" onClick={checkJoinCode}>
                  JOIN CLASS
                </Button>
              </Col>
            </Form.Row>
          </Form>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ClassCard;
