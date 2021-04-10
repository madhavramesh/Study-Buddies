import React from 'react';
import { Button, Card, Form, FormControl, Nav } from 'react-bootstrap';
import './ClassCard.scss';

type ClassProps = {
  name: string;
  number: string;
  term: string;
  desc: string;
  owner: string;
};

const ClassCard: React.FC<ClassProps> = ({ name, number, desc, term, owner }) => {
  return (
    <>
      <Card style={{ width: '18rem' }} className="CardOutline">
        <Card.Body className="CardBody">
          <Card.Title>{name}</Card.Title>
          <Card.Subtitle className="mb-2 text-muted">
            {number} | {term} <br />
            {owner}
          </Card.Subtitle>
          <Card.Text>{desc}</Card.Text>
        </Card.Body>
      </Card>
    </>
  );
};

export default ClassCard;
