import React, { useEffect, useState } from 'react';
import { Accordion, Button, Card } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import './StudentDashboard.scss';
import StudentInfo from '../../components/StudentInfo';
import PreferencesButton from '../../components/PreferencesButton';
import StudyGroupDisplay from '../../components/StudyGroupDisplay';

const axios = require('axios');

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

const IMG_WIDTH = 600;
const IMG_HEIGHT = 250;
const RANDOM_IMAGE_URL = `https://source.unsplash.com/featured/${IMG_WIDTH}x${IMG_HEIGHT}/?dark, study, class`;

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

  const [students, setStudents] = useState<any>([]);

  const [dormPreference, setDormPreference] = useState('');
  const [selectedPeoplePreference, setSelectedPeoplePreference] = useState<Array<number>>([]);
  const [selectedTimesPreference, setSelectedTimesPreference] = useState<Array<number>>([]);
  const [groupId, setGroupId] = useState('');

  const [studyGroups, setStudyGroups] = useState([]);

  const [classCreatedShowModal, setClassCreatedShowModal] = useState(true);
  const [preferencesShowModal, setPreferencesShowModal] = useState(false);

  const [accordionArrowDown, setAccordionArrowDown] = useState(false);

  const getPersonInfo = (id: string | null) => {
    axios
      .get(`http://localhost:4567/person_info/${id}`, CONFIG)
      .then((response: any) => {
        const { data } = response;
        setFirstName(data.first_name);
        setLastName(data.last_name);
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
      })
      .catch((_: any) => {
        history.push('/error');
      });
  };

  const getStudents = () => {
    axios
      .get(`http://localhost:4567/get_persons_in/${classID}`, CONFIG)
      .then((response: any) => {
        setStudents(response.data.persons);

        const studentIDs = response.data.persons.map((person: any) => person.id.toString());
        if (!studentIDs?.includes(sessionStorage.getItem('user_id'))) {
          history.push('/error');
        }
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
        }
      })
      .catch((err: any) => {
        console.log(err);
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
    setGroupId(response.data.preferences.groupId);
  };

  const getCurrentStudyGroups = () => {
    if (groupId != null && parseInt(groupId, 10) !== -1) {
      axios
        .get(`http://localhost:4567/get_persons_in/${groupId}/${classID}`)
        .then((response: any) => {
          setStudyGroups(response.data.persons);
        })
        .catch((err: any) => console.log(err));
    }
  };

  const getStudyGroupStudents = (studyGroup: any) => {
    return studyGroup.map((s: any) => `${s.firstName} ${s.lastName}`);
  };

  useEffect(() => {
    const id = sessionStorage.getItem('user_id');
    getPersonInfo(id);
    getClassInfo();
    getStudents();
    getPreferences();
    getCurrentStudyGroups();
  }, []);

  useEffect(() => {
    getPreferences();
  }, [preferencesShowModal]);

  return (
    <div className="student-dashboard-page">
      <ModifiedNavBar username={username} />

      <div className="class-joined-modal-container">
        <ClassCreatedModal
          onHide={() => setClassCreatedShowModal(false)}
          show={classCreatedShowModal}
          classNumber={classNumber}
          className={className}
        />
      </div>

      <div className="student-dashboard-page-sections">
        <div className="page-section study-groups-and-preferences">
          <div className="study-groups">
            <div className="study-groups-header">
              <Card>
                <Card.Header>Study Groups</Card.Header>
              </Card>
            </div>
            <div className="study-groups-body">
              <StudyGroupDisplay
                groupID={groupId}
                studentNames={getStudyGroupStudents(studyGroups)}
                imageURL={RANDOM_IMAGE_URL}
              />
            </div>
          </div>
          <div className="current-preferences">
            <div className="current-preferences-header">
              <Accordion>
                <Card>
                  <Accordion.Toggle
                    as={Card.Header}
                    onClick={() => setAccordionArrowDown(!accordionArrowDown)}
                    eventKey="0"
                  >
                    Current Preferences
                    <div
                      className={
                        accordionArrowDown
                          ? 'accordion arrow arrow-up'
                          : 'accordion arrow arrow-down'
                      }
                    />
                  </Accordion.Toggle>
                  <Accordion.Collapse eventKey="0">
                    <Card.Body>
                      <div className="current-preferences-body">
                        <div className="dorm">
                          <div className="dorm-header">Dorm</div>
                          {dormPreference && (
                            <div className="dorm-body">
                              &nbsp;&nbsp;&nbsp;&nbsp;{dormPreference}
                            </div>
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
                          <div className="times-body">
                            &nbsp;&nbsp;&nbsp;&nbsp;Please view modal
                          </div>
                        </div>
                      </div>
                    </Card.Body>
                  </Accordion.Collapse>
                </Card>
              </Accordion>
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
          <div className="page-section select-preferences">
            <PreferencesButton
              className={className}
              classNumber={classNumber}
              classID={classID}
              classTerm={classTerm}
              showModal={preferencesShowModal}
              setShowModal={setPreferencesShowModal}
            />
          </div>
        </div>

        <div className="page-section students">
          <div className="students-header">
            <Card>
              <Card.Header>Students</Card.Header>
            </Card>
          </div>
          <div className="students-body">
            {students.map((student: any) => (
              <StudentInfo
                studentName={`${student.firstName} ${student.lastName}`}
                // eslint-disable-next-line @typescript-eslint/no-empty-function
                removeStudent={() => {}}
                removeButton={false}
                studentDashboard
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
