import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Button, Form, Modal } from 'react-bootstrap';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import PersonCard from '../PersonCard';
import TimesPane from '../TimesPane';
import './PreferencesButton.scss';
import SearchBar from '../SearchBar/SearchBar';

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

function deserializePersonPreferences(persons: any, selected: Array<number>): Array<number> {
  const newSelected = new Array(persons.length).fill(0);
  // eslint-disable-next-line array-callback-return
  persons?.map((person: any, index: number) => {
    if (selected?.includes(person.id)) {
      newSelected[index] = 1;
    } else if (selected?.includes(-person.id)) {
      newSelected[index] = -1;
    }
  });
  return newSelected;
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

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

type PreferencesButtonProps = {
  className: string;
  classNumber: string;
  classID: string;
  classTerm: string;
  showModal: boolean;
  setShowModal: any;
};

const PreferencesButton: React.FC<PreferencesButtonProps> = ({
  className,
  classNumber,
  classID,
  classTerm,
  showModal,
  setShowModal,
}: PreferencesButtonProps) => {
  const [page, setPage] = useState(0);

  const [dorm, setDorm] = useState('Select a dorm...');

  const [searchText, setSearchText] = useState('');
  const [allPersons, setAllPersons] = useState([]);
  const [persons, setPersons] = useState<Array<any>>([]);
  const [selected, setSelected] = useState<Array<number>>([]);

  const [selectedTimes, setSelectedTimes] = useState(initialTimes);

  const username = `${sessionStorage.getItem('first_name')} ${sessionStorage.getItem('last_name')}`;

  const submitPreferences = async () => {
    const postParameters = {
      person_id: sessionStorage.getItem('user_id'),
      class_id: classID,
      dorm,
      person_preferences: serializePersonPreferences(persons, selected),
      time_preferences: serializeTimePreferences(selectedTimes),
    };

    const response = await axios.post(
      'http://localhost:4567/set_preferences',
      postParameters,
      CONFIG
    );
    setShowModal(false);
    setPage(0);
  };

  const getInitialPrefPplInfo = async () => {
    let response = await axios.get(`http://localhost:4567/get_persons_in/${classID}`, CONFIG);
    const { persons: currentPersons } = response.data;
    console.log(currentPersons);
    setAllPersons(currentPersons);
    setPersons(currentPersons);
    response = await axios.get(
      `http://localhost:4567/get_person_pref_in/${classID}/${sessionStorage.getItem('user_id')!}`,
      CONFIG
    );
    setDorm(response.data.preferences.dorm ?? '');
    const newSelected = deserializePersonPreferences(
      currentPersons,
      response.data.preferences.preferences
    );
    setSelected(newSelected ?? []);
    setSelectedTimes(response.data.preferences.times ?? []);
  };

  useEffect(() => {
    getInitialPrefPplInfo();
  }, []);

  let personCards: JSX.Element[] = [];

  if (persons.length) {
    const filteredPersons = persons.filter(
      (person) => `${person.firstName} ${person.lastName}` === username
    );
    personCards = filteredPersons.map((person: any, index) => {
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

  const filterPersons = (words: Array<string>, prefix: string) => {
    const preLowercase = prefix.toLowerCase();
    const nameMatches = words.filter((p: any) =>
      (p.firstName + p.lastName).toLowerCase().trim().startsWith(preLowercase)
    );
    return nameMatches;
  };

  useEffect(() => {
    let modifiedPersons: Array<string> = [];
    modifiedPersons = filterPersons(allPersons, searchText);
    setPersons(modifiedPersons);
  }, [searchText]);

  let displayedPage: any = '';
  switch (page) {
    case 0:
      displayedPage = (
        <>
          <Modal.Header closeButton>
            <b className="bold-style">Select your preferences</b>
          </Modal.Header>
          <Modal.Body className="modal-body-intro">
            <MenuBookIcon className="menu-book-icon" />
            <div>
              Welcome to{' '}
              <b>
                [{classNumber}] {className}
              </b>{' '}
              in {classTerm}! Choose your preferences, then wait to be put into a group.
            </div>
          </Modal.Body>
          <Modal.Footer>
            <Button className="page-navigate-button" onClick={() => setPage(page + 1)}>
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
            <b className="bold-style">Choose your dorm</b>
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
                value={dorm}
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
            <Button
              className="clear-preferences-button"
              onClick={() => setDorm('Select a dorm...')}
            >
              Clear Preferences
            </Button>
            <Button className="page-navigate-button" onClick={() => setPage(page - 1)}>
              ⬅ Introduction
            </Button>
            <Button
              className="page-navigate-button"
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
            <b className="bold-style">Select preferences for other people</b>
          </Modal.Header>
          <Modal.Body className="modal-body-people">
            <div className="modal-body-people-text">
              Select people you want to work with (<span style={{ color: '#3cb371' }}>green</span>),
              or feel uncomfortable working with (
              <span style={{ color: 'var(--slim-red)' }}>red</span>). Click multiple times to cycle
              through options.
            </div>
            <div className="search-bar-persons">
              <SearchBar
                onChange={(e: any) => setSearchText(e.target.value)}
                placeholderText="Search for people"
                searchInstructions="Search for classes by first name"
                showSearchHeader={false}
                showSearchDescription={false}
              />
            </div>
            {personCards}
          </Modal.Body>
          <Modal.Footer>
            <Button
              className="clear-preferences-button"
              onClick={() => setSelected(new Array(persons.length).fill(0))}
            >
              Clear Preferences
            </Button>
            <Button className="page-navigate-button" onClick={() => setPage(page - 1)}>
              ⬅ Dorm
            </Button>
            <Button className="page-navigate-button" onClick={() => setPage(page + 1)}>
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
            <b className="bold-style">Select preferred working times</b>
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
            <Button
              className="clear-preferences-button"
              onClick={() =>
                setSelectedTimes(new Array(7).fill(0).map(() => new Array(24).fill(0)))
              }
            >
              Clear Preferences
            </Button>
            <Button className="page-navigate-button" onClick={() => setPage(page - 1)}>
              ⬅ People
            </Button>
            <Button className="page-navigate-button" onClick={submitPreferences}>
              Submit!
            </Button>
          </Modal.Footer>
        </>
      );
      break;
    default:
      break;
  }

  return (
    <div>
      <Button className="modal-button" type="info" onClick={() => setShowModal(true)}>
        Select Preferences
      </Button>
      <Modal
        size="lg"
        show={showModal}
        onHide={() => {
          setShowModal(false);
          setPage(0);
        }}
        style={{ height: '80vh' }}
        dialogClassName="modal-80h"
        centered
        scrollable
      >
        {displayedPage}
      </Modal>
    </div>
  );
};

export default PreferencesButton;
