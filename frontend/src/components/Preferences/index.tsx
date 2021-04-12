import React from 'react';
import './PartnerPreferencesElements.scss';
import { ListGroup } from 'react-bootstrap';

import ClassCard from '../ClassCard';

const Preferences: React.FC = () => {
  const people = [
    { FirstName: 'Madhav', LastName: 'Ramesh' },
    { FirstName: 'Richard', LastName: 'Tang' },
    { FirstName: 'Jack', LastName: 'Cheng' },
    { FirstName: 'Akash', LastName: 'Singirikonda' },
  ];

  return (
    <>
      <ListGroup>
        {people.map((person) => (
          <ClassCard
            cid={1}
            name={person.FirstName}
            number={person.LastName}
            term="Spring"
            desc="This is weird"
            classCode="123123"
            owner="guys"
          />
        ))}
      </ListGroup>
    </>
  );

  /*
    <>
      <ListGroup>
        {people.map((person) => (
          <ListGroup.Item>
            {person.FirstName} {person.LastName}
          </ListGroup.Item>
        ))}
      </ListGroup>
    </>
  );
   */
};

export default Preferences;
