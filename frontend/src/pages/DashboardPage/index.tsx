import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
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
  const [allClasses, setAllClasses] = useState([]);
  const [classes, setClasses] = useState([]);

  const getInitialClasses = () => {
    axios
      .get('http://localhost:4567/get_all_classes', CONFIG)
      .then((response) => {
        setAllClasses(response.data.classes);
        setClasses(response.data.classes);
        console.log(response.data.classes);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const filterClasses = (prefix: string) => {
    const preLowercase = prefix.toLowerCase();
    return allClasses.filter((c: any) => c.className.toLowerCase().trim().startsWith(preLowercase));
  };

  useEffect(() => {
    getInitialClasses();
  }, []);

  useEffect(() => {
    console.log(searchText);
    const modifiedClasses = filterClasses(searchText);
    console.log(modifiedClasses);
    setClasses(modifiedClasses);
  }, [searchText]);

  return (
    <div className="dashboard-page">
      <ModifiedNavBar username="Madhav Ramesh" />
      <div className="search-bar-container">
        <div className="search-bar-inner-container">
          <div className="search-bar">
            <SearchBar onChange={(e: any) => setSearchText(e.target.value)} />
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
