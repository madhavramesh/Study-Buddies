import React, { useEffect, useState } from 'react';
import { Button, Modal, Table } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import StudentInfo from '../../components/StudentInfo';
import StudyGroupDisplay from '../../components/StudyGroupDisplay';
import './OwnersDashboard.scss';
import PreferencesButton from '../../components/PreferencesButton';
import AlgorithmVisualizer from '../../components/AlgorithmVisualizer';

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
const RANDOM_IMAGE_URL = `https://source.unsplash.com/featured/${IMG_WIDTH}x${IMG_HEIGHT}/?dark, study, class`;

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

  const [dormPreference, setDormPreference] = useState('');
  const [selectedPeoplePreference, setSelectedPeoplePreference] = useState<Array<number>>([]);
  const [selectedTimesPreference, setSelectedTimesPreference] = useState<Array<number>>([]);

  const [classCreatedShowModal, setClassCreatedShowModal] = useState(true);
  const [preferencesShowModal, setPreferencesShowModal] = useState(false);
  const [algorithmShowModal, setAlgorithmShowModal] = useState(false);

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

        // eslint-disable-next-line eqeqeq
        if (response.data.owner_id != sessionStorage.getItem('user_id')) {
          history.push('/error');
        }
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
        console.log(response.data);
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

  const getPreferences = async () => {
    const response = await axios.get(
      `http://localhost:4567/get_person_pref_in/${classID}/${sessionStorage.getItem('user_id')!}`,
      CONFIG
    );
    setDormPreference(response.data.preferences.dorm ?? '');
    setSelectedPeoplePreference(response.data.preferences.preferences ?? []);
    setSelectedTimesPreference(response.data.preferences.times ?? []);
    console.log(selectedTimesPreference);
  };

  useEffect(() => {
    getClassInfo();
    getStudents();
    getPreferences();
    console.log(selectedPeoplePreference);
  }, []);

  useEffect(() => {
    getPreferences();
  }, [preferencesShowModal]);

  return (
    <div className="owner-dashboard-page">
      <ModifiedNavBar username={username} />

      <div className="class-created-modal-container">
        <ClassCreatedModal
          onHide={() => setClassCreatedShowModal(false)}
          show={classCreatedShowModal}
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
              return (
                <StudyGroupDisplay
                  groupID={studyGroup[0].first}
                  studentNames={studyGroupNames}
                  imageURL={RANDOM_IMAGE_URL}
                />
              );
            })}
          </div>
          <div className="create-groups-container">
            <Button className="create-study-groups-button" onClick={formStudyGroups}>
              Generate Study Groups
            </Button>
          </div>
          <div className="visualize-groups-container">
            <AlgorithmVisualizer
              studyGroups={studyGroups}
              studyGroupWeights={studyGroupWeights}
              showModal={algorithmShowModal}
              setShowModal={setAlgorithmShowModal}
            />
          </div>
          <div className="current-preferences">
            <div className="current-preferences-header">Current Preferences</div>
            <div className="current-preferences-body">
              <div className="dorm">
                <div className="dorm-header">Dorm</div>
                {dormPreference && (
                  <div className="dorm-body">&nbsp;&nbsp;&nbsp;&nbsp;{dormPreference}</div>
                )}
              </div>
              <div className="preferred-people">
                <div className="preferred-people-header">Preferred People</div>
                <div className="preferred-people-body">
                  {selectedPeoplePreference.reduce((acc: Array<any>, elt: number) => {
                    const matchingStudent: any = students.find(
                      (student: any) => student?.id === elt
                    );
                    return elt > 0
                      ? acc.concat([
                          <div className="preferred-person">
                            &nbsp;&nbsp;&nbsp;&nbsp;{matchingStudent?.firstName}{' '}
                            {matchingStudent?.lastName}
                          </div>,
                        ])
                      : acc;
                  }, [])}
                </div>
              </div>
              <div className="not-preferred-people">
                <div className="not-preferred-people-header">Not Preferred People</div>
                <div className="not-preferred-people-body">
                  {selectedPeoplePreference.reduce((acc: Array<any>, elt: number) => {
                    const matchingStudent: any = students.find(
                      (student: any) => student?.id === Math.abs(elt)
                    );
                    return elt < 0
                      ? acc.concat([
                          <div className="not-preferred-person">
                            &nbsp;&nbsp;&nbsp;&nbsp;{matchingStudent?.firstName}{' '}
                            {matchingStudent?.lastName}
                          </div>,
                        ])
                      : acc;
                  }, [])}
                </div>
              </div>
              <div className="times">
                <div className="times-header">Preferred Times</div>
                &nbsp;&nbsp;&nbsp;&nbsp;Please view modal
              </div>
            </div>
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
          <PreferencesButton
            className={className}
            classNumber={classNumber}
            classID={classID}
            classTerm={classTerm}
            showModal={preferencesShowModal}
            setShowModal={setPreferencesShowModal}
          />
        </div>

        <div className="page-section students">
          <div className="students-header">Students</div>
          <div className="students-body">
            {students.map((student: any) => (
              <StudentInfo
                studentName={`${student.firstName} ${student.lastName}`}
                removeStudent={() => removeStudent(student.id)}
                removeButton={student.id !== classOwnerID}
                studentDashboard={false}
              />
            ))}
            <div className="leave-class-container">
              <Button className="leave-class-button" onClick={deleteClass}>
                Delete Class
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OwnerDashboardPage;
