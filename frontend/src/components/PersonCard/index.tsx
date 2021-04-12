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
    <Card className={`person-card ${background}`} onClick={handleClick}>
      {/* <Card.Title className="person-title"> */}
      <div className="person-title">
        {firstName} {lastName}
      </div>
      {/* </Card.Title> */}
      <div className="bottom-right-id">{id}</div>
    </Card>
  );
};

export default PersonCard;
