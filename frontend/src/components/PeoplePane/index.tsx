import React, { useState, useEffect } from 'react';
import axios from 'axios';
import PersonCard from '../PersonCard';

const PeoplePane: React.FC = () => {
  const [persons, setPersons] = useState([]);
  const [selected, setSelected] = useState([]);

  const getPeopleInClass = async () => {
    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };
    const response = await axios.get(`http://localhost:4567/get_persons_in/${1}`, config);
    setPersons(response.data.persons);
    const selectedArray: any = new Array(response.data.persons.length).fill(0);
    setSelected(selectedArray);
  };

  useEffect(() => {
    getPeopleInClass();
  }, []);

  let personCards: JSX.Element[] = [];

  if (persons.length) {
    personCards = persons.map((person: any, index) => {
      return (
        <PersonCard
          firstName={person.firstName}
          lastName={person.lastName}
          id={person.id}
          selectedValue={selected[index]}
          handleClick={() => {
            const newSelected: any = [...selected];
            newSelected[index] = newSelected[index] === 1 ? -1 : newSelected[index] + 1;
            setSelected(newSelected);
          }}
        />
      );
    });
  }

  return <div>{personCards}</div>;
};

export default PeoplePane;
