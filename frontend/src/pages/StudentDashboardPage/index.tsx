import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import './StudentDashboard.scss';
import StudentInfo from '../../components/StudentInfo';
import PreferencesButton from '../../components/PreferencesButton';

const axios = require('axios');

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

const StudentDashboardPage: React.FC = ({ match }: any) => {
  const {
    params: { classID },
  } = match;

  const history = useHistory();

  const username = `${sessionStorage.getItem('first_name')} ${sessionStorage.getItem('last_name')}`;

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');

  const [className, setClassName] = useState('');
  const [classNumber, setClassNumber] = useState('');
  const [classDescription, setClassDescription] = useState('');
  const [classTerm, setClassTerm] = useState('');
  const [classCode, setClassCode] = useState('');
  const [classOwnerID, setClassOwnerID] = useState('');

  const [students, setStudents] = useState([]);
  const getPersonInfo = (id: string | null) => {
    axios
      .get(`http://localhost:4567/person_info/${id}`, CONFIG)
      .then((response: any) => {
        const { data } = response;
        setFirstName(data.first_name);
        setLastName(data.last_name);
        console.log(response.data);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

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
          history.push('/dashboard');
          console.log('User successfully removed');
        } else {
          console.log('User not allowed to be on this page');
        }
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  useEffect(() => {
    const id = sessionStorage.getItem('user_id');
    getPersonInfo(id);
    getClassInfo();
    getStudents();
    students.map((student) => {
      console.log(student);
      return 0;
    });
  }, []);

  const [modalShow, setModalShow] = useState(true);

  return (
    <div className="student-dashboard-page">
      <ModifiedNavBar username={username} />

      <div className="class-joined-modal-container">
        <ClassCreatedModal
          onHide={() => setModalShow(false)}
          show={modalShow}
          classNumber={classNumber}
          className={className}
        />
      </div>

      <div className="student-dashboard-page-sections">
        <div className="page-section general-info">
          <GeneralInfoClass
            className={className}
            classNumber={classNumber}
            classDescription={classDescription}
            classTerm={classTerm}
            classCode={classCode}
          />
          <div className="page-section select-preferences">
            <PreferencesButton />
          </div>
        </div>

        <div className="page-section students">
          <div className="students-header">Students</div>
          <div className="students-body">
            {students.map((student: any) => (
              <StudentInfo
                studentName={`${student.firstName} ${student.lastName}`}
                removeStudent={() => console.log('No remove student')}
                removeButton={false}
              />
            ))}
          </div>
          <div className="leave-class-container">
            <Button
              className="leave-class-button"
              onClick={() => removeStudent(sessionStorage.getItem('user_id')!)}
            >
              Leave Class
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StudentDashboardPage;

/*
const leaveClass = () => {
    const postParameters = {
      id: sessionStorage.get('user_id'),
      class_id: { classID },
    };

    axios
      .post(`http://localhost:4567/leave_class`, postParameters, CONFIG)
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
 */
