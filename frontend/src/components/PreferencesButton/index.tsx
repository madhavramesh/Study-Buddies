import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Button, Form, Modal } from 'react-bootstrap';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import PersonCard from '../PersonCard';
import TimesPane from '../TimesPane';
import './PreferencesButton.scss';

const dorms = [
  'Andrews Hall',
  'Metcalf Hall',
  'Miller Hall',
  'Morriss Hall',
  'Champlin Hall',
  'Emery-Woolley Dormitory',
  'Perkins Hall',
  'Keeney Quadrangle',
  'Gregorian Quad B',
  'Caswell Hall',
  'Slater Hall',
];

const initialTimes: Array<Array<number>> = new Array(7).fill(0).map(() => new Array(24).fill(0));

function serializePersonPreferences(persons: Array<any>, selected: Array<number>): string {
  let str = '';
  for (let i = 0; i < selected.length; i += 1) {
    if (selected[i] === 1) {
      str += `${persons[i].id},`;
    } else if (selected[i] === -1) {
      str += `-${persons[i].id},`;
    }
  }
  return str.length ? str.substring(0, str.length - 1) : str;
}

function serializeTimePreferences(selectedTimes: Array<Array<any>>): string {
  let str = '';
  for (let i = 0; i < selectedTimes.length; i += 1) {
    for (let j = 0; j < selectedTimes[i].length; j += 1) {
      str += selectedTimes[i][j];
    }
    str += ':';
  }
  return str.substring(0, str.length - 1);
}

const PreferencesButton: React.FC = () => {
  const className = 'Software Engineering';
  const classTerm = 'Spring 2021 🌺';

  const [showModal, setShowModal] = useState(false);
  const [page, setPage] = useState(0);

  const [dorm, setDorm] = useState('');

  const [persons, setPersons] = useState([]);
  const [selected, setSelected] = useState<Array<number>>([]);

  const [selectedTimes, setSelectedTimes] = useState(initialTimes);

  const submitPreferences = async () => {
    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };

    const postParameters = {
      person_id: sessionStorage.getItem('user_id'),
      class_id: 1,
      dorm,
      person_preferences: serializePersonPreferences(persons, selected),
      time_preferences: serializeTimePreferences(selectedTimes),
    };

    const response = await axios.post(
      'http://localhost:4567/set_preferences',
      postParameters,
      config
    );
    console.log(response.data);
  };

  const getPeopleInClass = async () => {
    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };
    const response = await axios.get(`http://localhost:4567/get_persons_in/${1}`, config);
    setPersons(response.data.persons);
    const selectedArray: Array<number> = new Array(response.data.persons.length).fill(0);
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

  let displayedPage: any = '';
  switch (page) {
    case 0:
      displayedPage = (
        <>
          <Modal.Header closeButton>
            <b>Select your preferences</b>
          </Modal.Header>
          <Modal.Body className="modal-body-intro">
            <MenuBookIcon className="menu-book-icon" />
            <div>
              Welcome to &quot;{className}&quot; in {classTerm}! Choose your preferences, then wait
              to be put into a group.
            </div>
          </Modal.Body>
          <Modal.Footer>
            <Button className="modal-button" onClick={() => setPage(page + 1)}>
              Dorm ➡
            </Button>
          </Modal.Footer>
        </>
      );
      break;
    case 1:
      displayedPage = (
        <>
          <Modal.Header closeButton>
            <b>Choose your dorm</b>
          </Modal.Header>
          <Modal.Body className="modal-body-dorm">
            <div className="modal-body-dorm-text">
              Enter your dorm to be put with people near your location!
            </div>
            <Form>
              <Form.Control
                as="select"
                onChange={(e: any) => setDorm(e.target.value)}
                isInvalid={dorm === 'Select a dorm...'}
                defaultValue="Select a dorm..."
              >
                <option>Select a dorm...</option>
                {dorms.map((d) => (
                  <option>{d}</option>
                ))}
              </Form.Control>
              <Form.Control.Feedback type="invalid">You must select a dorm!</Form.Control.Feedback>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button className="modal-button" onClick={() => setPage(page - 1)}>
              ⬅ Introduction
            </Button>
            <Button
              className="modal-button"
              disabled={!dorm || dorm === 'Select a dorm...'}
              onClick={() => setPage(page + 1)}
            >
              People ➡
            </Button>
          </Modal.Footer>
        </>
      );
      break;
    case 2:
      displayedPage = (
        <>
          <Modal.Header closeButton>
            <b>Select preferences for other people</b>
          </Modal.Header>
          <Modal.Body className="modal-body-people">
            <div className="modal-body-people-text">
              Select people you want to work with (green), or feel uncomfortable working with (red).
              Click multiple times to cycle through options.
            </div>
            {personCards}
          </Modal.Body>
          <Modal.Footer>
            <Button className="modal-button" onClick={() => setPage(page - 1)}>
              ⬅ Dorm
            </Button>
            <Button className="modal-button" onClick={() => setPage(page + 1)}>
              Times ➡
            </Button>
          </Modal.Footer>
        </>
      );
      break;
    case 3:
      displayedPage = (
        <>
          <Modal.Header closeButton>
            <b>Select preferred working times</b>
          </Modal.Header>
          <Modal.Body className="modal-body-times">
            <div className="modal-body-times-text">
              Select times in which you think you can work best, or times during which you prefer
              working. Drag to select multiple concurrent time slots.
            </div>
            <div className="modal-body-times-calendar">
              <TimesPane
                slotLength={60}
                selectedTimes={selectedTimes}
                setSelectedTimes={setSelectedTimes}
              />
            </div>
          </Modal.Body>
          <Modal.Footer>
            <Button className="modal-button" onClick={() => setPage(page - 1)}>
              ⬅ People
            </Button>
            <Button className="modal-button" onClick={submitPreferences}>
              Submit!
            </Button>
          </Modal.Footer>
        </>
      );
      break;
    default:
      break;
  }

  const formGroups = async () => {
    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };

    const response = await axios.get(`http://localhost:4567/form_groups/1/4`, config);
    console.log(response.data);
  };

  return (
    <div>
      <Button className="modal-button" type="info" onClick={() => setShowModal(true)}>
        Click me!
      </Button>
      <Modal
        size="lg"
        show={showModal}
        onHide={() => setShowModal(false)}
        style={{ height: '80vh' }}
        dialogClassName="modal-80h"
        centered
        scrollable
      >
        {displayedPage}
      </Modal>
      <Button type="info" onClick={formGroups}>
        Form groups!
      </Button>
    </div>
  );
};

export default PreferencesButton;
