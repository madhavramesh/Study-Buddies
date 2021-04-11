import React from 'react';
import { Button, Card, Form, FormControl, Nav } from 'react-bootstrap';
import './PeopleCard.scss';

type PeopleProps = {
  firstName: string;
  lastName: string;
  id: string;
};

const PeopleCard: React.FC<PeopleProps> = ({ firstName, lastName, id }) => {
  return (
    <>
      <Card style={{ width: '18rem' }} className="CardOutline">
        <Card.Body className="CardBody">
          <Card.Text>
            {firstName} {lastName} | {id}
          </Card.Text>
        </Card.Body>
      </Card>
    </>
  );
};

export default PeopleCard;
