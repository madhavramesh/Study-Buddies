import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import StudentInfo from '../../components/StudentInfo';
import StudyGroupDisplay from '../../components/StudyGroupDisplay';
import './OwnersDashboard.scss';

const axios = require('axios');

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

// Size of study groups to form
const GROUP_SIZE = 4;

const IMG_WIDTH = 430;
const IMG_HEIGHT = 200;
const RANDOM_IMAGE_URL = `https://source.unsplash.com/featured/${IMG_WIDTH}x${IMG_HEIGHT}/?dark, study`;

const OwnerDashboardPage: React.FC = ({ match }: any) => {
  const {
    params: { classID },
  } = match;

  const history = useHistory();

  const [className, setClassName] = useState('');
  const [classNumber, setClassNumber] = useState('');
  const [classDescription, setClassDescription] = useState('');
  const [classTerm, setClassTerm] = useState('');
  const [classCode, setClassCode] = useState('');
  const [classOwnerID, setClassOwnerID] = useState('');

  const [students, setStudents] = useState([]);
  const [studyGroups, setStudyGroups] = useState([]);

  const username = `${sessionStorage.getItem('first_name')} ${sessionStorage.getItem('last_name')}`;

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
        console.log(response.data);
        setStudyGroups(response.data.class);
      })
      .catch((err: any) => {
        console.log(err.response.data);
      });
  };

  const getStudyGroupStudents = (studyGroup: any) => {
    return studyGroup.map((s: any) => `${s.second.firstName} ${s.second.lastName}`);
  };

  const removeStudent = (studentID: string) => {
    const postParameters = {
      id: studentID,
      class_id: classID,
    };

    axios
      .post(`http://localhost:4567/leave_class`, postParameters, CONFIG)
      .then((response: any) => {
        if (response.data.status === 0) {
          const studentsCopy = [...students];
          setStudents(studentsCopy.filter((studentCopy: any) => studentCopy.id !== studentID));
          console.log('User successfully removed');
        } else {
          console.log('User not allowed to be on this page');
        }
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  const deleteClass = () => {
    const postParameters = {
      id: sessionStorage.getItem('user_id'),
      class_id: { classID },
    };

    axios
      .post(`http://localhost:4567/delete_class`, postParameters, CONFIG)
      .then((response: any) => {
        if (response.status === 0) {
          history.push('/dashboard');
        } else {
          console.log('User not allowed to be on this page');
        }
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  const [modalShow, setModalShow] = useState(true);

  useEffect(() => {
    getClassInfo();
    getStudents();
  }, []);

  return (
    <div className="owner-dashboard-page">
      <ModifiedNavBar username={username} />

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
            {studyGroups.map((studyGroup: any) => {
              const studyGroupNames = getStudyGroupStudents(studyGroup);
              console.log(studyGroupNames);
              return (
                <StudyGroupDisplay
                  groupID={studyGroup[0].first}
                  studentNames={studyGroupNames}
                  imageURL={RANDOM_IMAGE_URL}
                />
              );
            })}
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
                removeStudent={() => removeStudent(student.id)}
                removeButton
              />
            ))}
          </div>
          <div className="leave-class-container">
            <Button className="leave-class-button" onClick={deleteClass}>
              Leave Class
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OwnerDashboardPage;
