import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, ToggleButton } from 'react-bootstrap';
import axios from 'axios';
import CreateClass from '../../components/CreateClass';
import './DashboardPageStyle.scss';
import SearchBar from '../../components/SearchBar/SearchBar';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCardPane from '../../components/ClassCardPane';

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

const DashboardPage: React.FC = () => {
  const [modalShow, setModalShow] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [allClasses, setAllClasses] = useState<Array<string>>([]);
  const [allEnrolledClasses, setAllEnrolledClasses] = useState<Array<string>>([]);
  const [classes, setClasses] = useState<Array<string>>([]);

  const [radioValue, setRadioValue] = useState('1');
  const enrolledToggle = [
    { name: 'All', value: '1' },
    { name: 'Enrolled', value: '2' },
  ];

  const username = `${sessionStorage.getItem('first_name')} ${sessionStorage.getItem('last_name')}`;

  const getInitialClasses = () => {
    axios
      .get('http://localhost:4567/get_all_classes', CONFIG)
      .then((response) => {
        setAllClasses(response.data.classes);
        setClasses(response.data.classes);
        console.log(`All Classes: ${response.data.classes}`);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const getEnrolledClasses = () => {
    console.log(`USER ID: ${sessionStorage.getItem('user_id')}`);
    axios
      .get(`http://localhost:4567/get_enrollments/${sessionStorage.getItem('user_id')}`, CONFIG)
      .then((response) => {
        setAllEnrolledClasses(response.data.classes);
        console.log(`Enrolled Classes: ${response.data.classes}`);
      })
      .catch((err) => {
        console.log(err.response.data);
      });
  };

  const filterClasses = (words: Array<string>, prefix: string) => {
    const preLowercase = prefix.toLowerCase();
    return words.filter((c: any) => c.className.toLowerCase().trim().startsWith(preLowercase));
  };

  useEffect(() => {
    getInitialClasses();
    getEnrolledClasses();
  }, []);

  useEffect(() => {
    let modifiedClasses: Array<string> = [];
    if (radioValue === enrolledToggle[0].value) {
      modifiedClasses = filterClasses(allClasses, searchText);
    } else if (radioValue === enrolledToggle[1].value) {
      modifiedClasses = filterClasses(allEnrolledClasses, searchText);
    }
    setClasses(modifiedClasses);
  }, [searchText, radioValue]);

  return (
    <div className="dashboard-page">
      <ModifiedNavBar username={username} />
      <div className="search-bar-container">
        <div className="search-bar-inner-container">
          <div className="search-bar">
            <SearchBar onChange={(e: any) => setSearchText(e.target.value)} />
          </div>
          <div className="enrolled-toggle-container">
            <ButtonGroup toggle className="enrolled-toggle">
              {enrolledToggle.map((toggle: any, _: number) => (
                <ToggleButton
                  type="radio"
                  variant="secondary"
                  name="radio"
                  value={toggle.value}
                  checked={radioValue === toggle.value}
                  onChange={(e: any) => setRadioValue(e.currentTarget.value)}
                >
                  {toggle.name}
                </ToggleButton>
              ))}
            </ButtonGroup>
          </div>
          <div className="card-pane-container">
            <ClassCardPane classes={classes} />
          </div>
        </div>
      </div>

      <div className="create-class-container">
        <Button
          className="create-class-button"
          variant="primary"
          onClick={() => setModalShow(true)}
        >
          Create
          <br />
          Class
        </Button>
        <CreateClass show={modalShow} onHide={() => setModalShow(false)} />
      </div>
    </div>
  );
};

export default DashboardPage;
