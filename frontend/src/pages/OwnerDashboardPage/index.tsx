import React, { useEffect, useState } from 'react';
import { Button, Modal, Table } from 'react-bootstrap';
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

const IMG_WIDTH = 600;
const IMG_HEIGHT = 250;
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
  const [studyGroupWeights, setStudyGroupWeights] = useState([]);

  const username = `${sessionStorage.getItem('first_name')} ${sessionStorage.getItem('last_name')}`;

  // Modal code
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

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
      .get(`http://localhost:4567/form_groups/${classID}/${GROUP_SIZE}`, CONFIG)
      .then((response: any) => {
        console.log(response.data.class);
        setStudyGroups(response.data.class.first);
        setStudyGroupWeights(response.data.class.second);
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
      class_id: classID,
    };

    axios
      .post(`http://localhost:4567/delete_class`, postParameters, CONFIG)
      .then((response: any) => {
        if (response.data.status === 0) {
          history.push('/dashboard');
        } else {
          console.log('User not allowed to be on this page');
        }
      })
      .catch((err: any) => {
        console.log(err.response.data);
      });
  };

  useEffect(() => {
    getClassInfo();
    getStudents();
  }, []);

  // Renders a single group
  const renderGroup = (g: any, index: number) => {
    // map ids to persons
    const idToPerson: any = {};
    // eslint-disable-next-line array-callback-return
    g.map((person: any) => {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      idToPerson[person.second.id] = person;
    });

    console.log(idToPerson);

    const graphArray = [];

    const studyGroupWeight = studyGroupWeights[index];
    // eslint-disable-next-line guard-for-in,no-restricted-syntax
    for (const key in studyGroupWeight) {
      const row = [];
      // eslint-disable-next-line guard-for-in,no-restricted-syntax
      for (const key2 in studyGroupWeight[key]) {
        const newArray = [key, key2, studyGroupWeight[key][key2]];
        row.push(newArray);
      }
      row.sort((a: any, b: any) => a[1] - b[1]);
      graphArray.push(row);
    }

    graphArray.sort((a: any, b: any) => a[0] - b[0]);

    // eslint-disable-next-line no-plusplus
    for (let i = 0; i < graphArray.length; i++) {
      graphArray[i].splice(i, 0, []);
    }

    console.log(graphArray);

    const people = g.map((person: any) => idToPerson[person.second.id]);
    console.log(people);

    return (
      <Table striped bordered hover>
        <thead>
          <th> </th>
          {people.map((person: any) => {
            return (
              <th id={person.second.id}>
                {person.second.firstName} {person.second.lastName}
              </th>
            );
          })}
        </thead>
        {graphArray.map((row, rowIndex) => {
          console.log(row, rowIndex);
          return (
            <tr>
              <td>
                {people[rowIndex].second.firstName} {people[rowIndex].second.lastName}
              </td>
              {row.map((column, columnIndex) => {
                console.log(column, columnIndex);
                return <td>{column[2]}</td>;
              })}
            </tr>
          );
        })}
      </Table>
    );
  };

  return (
    <div className="owner-dashboard-page">
      <ModifiedNavBar username={username} />

      <div className="owner-dashboard-page-sections">
        <div className="page-section study-groups">
          <div className="study-groups-header">Study Groups</div>
          <div className="study-groups-body">
            {studyGroups.map((studyGroup: any) => {
              const studyGroupNames = getStudyGroupStudents(studyGroup);
              return (
                <StudyGroupDisplay
                  groupID={studyGroup[0].first}
                  studentNames={studyGroupNames}
                  imageURL={RANDOM_IMAGE_URL}
                />
              );
            })}
          </div>
          <Button className="view-algorithm-button" onClick={() => handleShow()}>
            View Algorithm
          </Button>
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
                removeButton={student.id !== classOwnerID}
              />
            ))}
            <div className="leave-class-container">
              <Button className="leave-class-button" onClick={deleteClass}>
                Leave Class
              </Button>
            </div>
          </div>
        </div>
      </div>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Algorithm Visualization</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div>Algorithm results</div>
          <div>
            {studyGroups.map((group, index) => {
              return renderGroup(group, index);
            })}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default OwnerDashboardPage;
