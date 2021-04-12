import React from 'react';
import { Button } from 'react-bootstrap';
import './OwnersDashboard.scss';

import axios from 'axios';
import NavBar from '../../components/Navbar';
import Footer from '../../components/Footer';

import GeneralInfo from '../../components/GeneralInfo';

type dashboardInput = {
  classID: number;
};

const OwnersDashboard: React.FC<dashboardInput> = ({ classID }) => {
  const getClassInfo = () => {
    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };

    axios
      .get(`http://localhost:4567/get_class_with/${classID}`, config)
      .then((response) => {
        console.log(response.data);
        /*
        const classId = theClass.class_id;
        const className = theClass.class_name;
        const classNumber = theClass.class_number;
        const classDescription = theClass.class_description;
        const classTerm = theClass.class_term;
        const classCode = theClass.class_code;
        const classOwnerId = theClass.owner_id;
        */
        // Note: It is very important that you understand how this is set up and why it works!
      })
      .catch(function (error) {
        console.log('ERROR');
        console.log(error);
      });
  };

  return (
    <div id="container">
      <NavBar />
      <div id="page">
        <div className="column">
          <div id="studyGroups">study groups go here</div>
          <Button className="button" size="sm">
            Form Groups
          </Button>
        </div>

        <div className="column">
          <div id="general-info">
            <GeneralInfo
              className="dummy class name"
              classNumber="wait for database"
              classDescription="this is where class description goes"
              classTerm="1"
              classCode="234"
              ownerID={123}
            />
          </div>
        </div>

        <div className="column">
          <div id="students">students go here</div>
          <Button className="button" size="sm">
            Delete Students
          </Button>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default OwnersDashboard;
