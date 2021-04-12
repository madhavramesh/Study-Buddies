import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import './OwnersDashboard.scss';
import StudentInfo from '../../components/StudentInfo';

const axios = require('axios');

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

const OwnerDashboardPage: React.FC = ({ match }: any) => {
  const {
    params: { classID },
  } = match;

  const [className, setClassName] = useState('');
  const [classNumber, setClassNumber] = useState('');
  const [classDescription, setClassDescription] = useState('');
  const [classTerm, setClassTerm] = useState('');
  const [classCode, setClassCode] = useState('');
  const [classOwnerID, setClassOwnerID] = useState('');

  const getClassInfo = () => {
    axios
      .get(`http://localhost:4567/get_class_with/${classID}`, CONFIG)
      .then((response: any) => {
        setClassName(response.data.class_name);
        setClassNumber(response.data.class_number);
        setClassDescription(response.data.class_description);
        setClassTerm(response.data.class_term);
        setClassCode(response.data.class_code);
        setClassOwnerID(response.data.owner_id);

        console.log(`Class ID: ${classID}`);
        console.log(response.data);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  const getStudents = () => {
    axios.get(`http://localhost:4567/get_class_with/${classID}`, CONFIG))
  }

  const [modalShow, setModalShow] = useState(true);

  useEffect(() => {
    getClassInfo();
    console.log(className);
  }, []);

  return (
    <div className="owner-dashboard-page">
      <ModifiedNavBar username="Madhav Ramesh" />

      <div className="class-created-modal-container">
        <ClassCreatedModal
          onHide={() => setModalShow(false)}
          show={modalShow}
          classNumber={classNumber}
          className={className}
        />
      </div>

      <div className="owner-dashboard-page-sections">
        <div className="page-section study-groups">
          <div className="study-groups-header">Study Groups</div>
          {/* <Button className="button" size="sm"> */}
          {/*  Form Groups */}
          {/* </Button> */}
        </div>

        <div className="page-section general-info">
          <GeneralInfoClass
            className={className}
            classNumber={classNumber}
            classDescription={classDescription}
            classTerm={classTerm}
            classCode={classCode}
          />
          <div className="create-groups-container">
            <Button className="create-study-groups-button">Create Study Groups</Button>
          </div>
        </div>

        <div className="page-section students">
          <div className="students-header">Students</div>
          <div className="students-body">
            <StudentInfo studentName="Blah Blah Blah Blah Blah" />
          </div>
          {/* <Button className="button" size="sm"> */}
          {/*  Delete Students */}
          {/* </Button> */}
        </div>
      </div>
    </div>
  );
};

export default OwnerDashboardPage;
