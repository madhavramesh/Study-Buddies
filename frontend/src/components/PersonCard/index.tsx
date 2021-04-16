import React from 'react';
import { Card, Form } from 'react-bootstrap';
import './PersonCard.scss';

type PersonProps = {
  firstName: string;
  lastName: string;
  id: number;
  selectedValue: number;
  handleClick: any;
};

const PersonCard: React.FC<PersonProps> = ({
  firstName,
  lastName,
  id,
  selectedValue,
  handleClick,
}) => {
  const background =
    // eslint-disable-next-line no-nested-ternary
    selectedValue === -1 ? 'not-preferred' : selectedValue === 1 ? 'preferred' : '';
  return (
    <Card
      className={`person-card ${background}`}
      style={{ width: '18rem', height: '7rem' }}
      onClick={handleClick}
    >
      <Card.Body>
        <Card.Title>
          {firstName} {lastName}
        </Card.Title>
        <Card.Text>{id}</Card.Text>
      </Card.Body>
    </Card>
  );
};

export default PersonCard;
