import React from 'react';
import { Button, Card, Form, FormControl, Nav } from 'react-bootstrap';
import './TimeCard.scss';

type TimeProps = {
  timeslot: string;
};

const TimeCard: React.FC<TimeProps> = ({ timeslot }) => {
  return (
    <>
      <Card style={{ width: '18rem' }} className="CardOutline">
        <Card.Body className="CardBody">
          <Card.Text>{timeslot}</Card.Text>
        </Card.Body>
      </Card>
    </>
  );
};

export default TimeCard;
