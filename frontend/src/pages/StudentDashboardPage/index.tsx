import React, { useEffect, useState } from 'react';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import './StudentDashboard.scss';
import StudentInfo from '../../components/StudentInfo';

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

  const [className, setClassName] = useState('');
  const [classNumber, setClassNumber] = useState('');
  const [classDescription, setClassDescription] = useState('');
  const [classTerm, setClassTerm] = useState('');
  const [classCode, setClassCode] = useState('');
  const [classOwnerID, setClassOwnerID] = useState('');

  const [students, setStudents] = useState([]);

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

  useEffect(() => {
    getClassInfo();
    getStudents();
    students.map((student) => {
      console.log(student);
      return 0;
    });
  }, []);

  return (
    <div>
      <ModifiedNavBar username={sessionStorage.getItem('user_id')} />
      <div className="column">
        <div id="top">
          <GeneralInfoClass
            className={className}
            classNumber={classNumber}
            classDescription={classDescription}
            classTerm={classTerm}
            classCode={classCode}
          />
        </div>
        <div id="bottom" />
      </div>

      <div className="column" />
    </div>
  );
};

export default StudentDashboardPage;
