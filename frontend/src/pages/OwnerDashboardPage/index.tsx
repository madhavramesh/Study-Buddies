import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import './OwnersDashboard.scss';
import StudentInfo from '../../components/StudentInfo';
import StudyGroupDisplay from '../../components/StudyGroupDisplay';

const axios = require('axios');

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

// Size of study groups to form
const GROUP_SIZE = 4;

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

  const [students, setStudents] = useState([]);
  const [studyGroups, setStudyGroups] = useState([]);

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
    axios
      .get(`http://localhost:4567/get_persons_in/${classID}`, CONFIG)
      .then((response: any) => {
        setStudents(response.data.persons);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  const formStudyGroups = () => {
    axios
      .get(`http://localhost:4567/form_groups/${classID}/${4}`, CONFIG)
      .then((response: any) => {
        setStudyGroups(response.data);
      })
      .catch((err: any) => {
        console.log(err.response.data);
      });
  };

  const [modalShow, setModalShow] = useState(true);

  useEffect(() => {
    getClassInfo();
    getStudents();
    students.map((student) => {
      console.log(student);
      return 0;
    });
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
          <div className="study-groups-body">
            {studyGroups.map((studyGroup) => (
              <StudyGroupDisplay groupID="sample" studentNames={studyGroup} />
            ))}
          </div>
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
            <Button className="create-study-groups-button" onClick={formStudyGroups}>
              Create Study Groups
            </Button>
          </div>
        </div>

        <div className="page-section students">
          <div className="students-header">Students</div>
          <div className="students-body">
            {students.map((student: any) => (
              <StudentInfo
                studentName={`${student.firstName} ${student.lastName}`}
                removeStudent={() => console.log('Removing')}
              />
            ))}
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
