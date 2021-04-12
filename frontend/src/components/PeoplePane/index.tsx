import React, { useState } from 'react';
import './PeoplePaneElements.scss';
import { ListGroup } from 'react-bootstrap';
import PeopleCard from '../PeopleCard';

type personType = {
  firstName: string;
  lastName: string;
  id: number;
};

type peopleProps = {
  people: Array<personType>;
};

const testPeople = [
  { firstName: 'Madhav', lastName: 'Ramesh', id: 1 },
  { firstName: 'Richard', lastName: 'Tang', id: 2 },
  { firstName: 'Jack', lastName: 'Cheng', id: 3 },
  { firstName: 'Akash', lastName: 'Singirikonda', id: 4 },
];

const Preferences: React.FC<peopleProps> = ({ people }) => {
  const [isPreferred, setIsPreferred] = useState(true);
  const [preferredPeople, setPreferredPeople] = useState([]);

  function selectPerson(e: any) {
    console.log(e);
  }
  return (
    <>
      <ListGroup>
        {people.map((person) => (
          <PeopleCard
            handleClick={selectPerson}
            firstName={person.firstName}
            lastName={person.lastName}
            id={person.id}
          />
        ))}
      </ListGroup>
    </>
  );
};

export default Preferences;
