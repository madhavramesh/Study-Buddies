import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
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

  const [classCreatedShowModal, setClassCreatedShowModal] = useState(true);
  const [preferencesShowModal, setPreferencesShowModal] = useState(false);

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
  };

  useEffect(() => {
    const id = sessionStorage.getItem('user_id');
    getPersonInfo(id);
    getClassInfo();
    getStudents();
    getPreferences();
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
            <div className="study-groups-header">Study Group</div>
            <div className="study-groups-body" />
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
          <div className="students-header">Students</div>
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
