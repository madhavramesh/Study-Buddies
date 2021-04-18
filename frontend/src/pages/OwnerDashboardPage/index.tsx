import React, { useEffect, useRef, useState } from 'react';
import { Accordion, Button, Card, Modal, OverlayTrigger, Popover } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import { CSVLink } from 'react-csv';
import GetAppIcon from '@material-ui/icons/GetApp';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import ClassCreatedModal from '../../components/ClassCreatedModal';
import GeneralInfoClass from '../../components/GeneralInfoClass';
import StudentInfo from '../../components/StudentInfo';
import StudyGroupDisplay from '../../components/StudyGroupDisplay';
import AlgorithmVisualizer from '../../components/AlgorithmVisualizer';
import PreferencesButton from '../../components/PreferencesButton';
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
  const [studyGroupsCSV, setStudyGroupsCSV] = useState<Array<Array<string>>>([]);
  const [studyGroupsCSVReady, setStudyGroupsCSVReady] = useState(false);
  const [studyGroupWeights, setStudyGroupWeights] = useState([]);

  const csvLink = useRef<any>();

  const username = `${sessionStorage.getItem('first_name')} ${sessionStorage.getItem('last_name')}`;

  const [dormPreference, setDormPreference] = useState('');
  const [selectedPeoplePreference, setSelectedPeoplePreference] = useState<Array<number>>([]);
  const [selectedTimesPreference, setSelectedTimesPreference] = useState<Array<number>>([]);

  const [classCreatedShowModal, setClassCreatedShowModal] = useState(true);
  const [preferencesShowModal, setPreferencesShowModal] = useState(false);
  const [algorithmShowModal, setAlgorithmShowModal] = useState(false);
  const [deleteClassShowModal, setDeleteClassShowModal] = useState(false);

  const [accordionArrowDown, setAccordionArrowDown] = useState(false);

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
        setStudyGroups(response.data.class.first);
        console.log(response.data.class);
        setStudyGroupWeights(response.data.class.second);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  const getStudyGroupStudents = (studyGroup: any) => {
    return studyGroup.map((s: any) => `${s.second.firstName} ${s.second.lastName}`);
  };

  const getCurrentStudyGroups = () => {
    axios
      .get(`http://localhost:4567/get_groups_in/${classID}`, CONFIG)
      .then((response: any) => {
        console.log('Same');
        console.log(response.data);
        setStudyGroups(response.data.first);
        setStudyGroupWeights(response.data.second);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  const createStudyGroupsCSV = (groups: any) => {
    const studyGroupsFormatted = groups.map((group: any) => getStudyGroupStudents(group));
    setStudyGroupsCSV(studyGroupsFormatted);
    setStudyGroupsCSVReady(true);
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
  };

  useEffect(() => {
    getClassInfo();
    getStudents();
    getPreferences();
    getCurrentStudyGroups();
  }, []);

  useEffect(() => {
    getPreferences();
  }, [preferencesShowModal]);

  useEffect(() => {
    console.log(studyGroupsCSV);
    if (studyGroupsCSVReady) {
      csvLink?.current?.link?.click();
      setStudyGroupsCSVReady(false);
    }
  }, [studyGroupsCSVReady]);

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
          <div className="study-groups-header">
            <Card>
              <Card.Header>
                Study Groups
                <OverlayTrigger
                  overlay={
                    <Popover id="groups-csv-description">
                      <Popover.Content>Click to download as CSV!</Popover.Content>
                    </Popover>
                  }
                >
                  <GetAppIcon
                    className="groups-csv-button"
                    fontSize="large"
                    onClick={() => createStudyGroupsCSV(studyGroups)}
                  />
                </OverlayTrigger>
                <CSVLink
                  data={studyGroupsCSV}
                  filename="studygroups.csv"
                  target="_blank"
                  className="hidden"
                  ref={csvLink}
                />
              </Card.Header>
            </Card>
          </div>
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
          <div className="students-header">
            <Card>
              <Card.Header>Students</Card.Header>
            </Card>
          </div>
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
              <Button className="leave-class-button" onClick={() => setDeleteClassShowModal(true)}>
                Delete Class
              </Button>
              <Modal
                onHide={() => setDeleteClassShowModal(false)}
                show={deleteClassShowModal}
                scrollable
                centered
                className="delete-class-modal"
              >
                <Modal.Body className="delete-class-modal-body">
                  Are you sure you want to delete{' '}
                  <b>
                    [{classNumber}] {className}?
                  </b>
                </Modal.Body>
                <Modal.Footer>
                  <Button
                    className="delete-class-confirmation-button cancel"
                    onClick={() => setDeleteClassShowModal(false)}
                  >
                    Cancel
                  </Button>
                  <Button className="delete-class-confirmation-button yes" onClick={deleteClass}>
                    Yes
                  </Button>
                </Modal.Footer>
              </Modal>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OwnerDashboardPage;
