import React from 'react';
import { Button, InputGroup } from 'react-bootstrap';
import './StudentInfoStyle.scss';
import RemoveIcon from '@material-ui/icons/Remove';
import { Remove } from '@material-ui/icons';

type StudentInfoProps = {
  studentName: string;
  removeStudent: any;
  removeButton: boolean;
  studentDashboard: boolean;
};

const StudentInfo: React.FC<StudentInfoProps> = ({
  studentName,
  removeStudent,
  removeButton,
  studentDashboard,
}: StudentInfoProps) => {
  return (
    <div className={`student ${studentName}`}>
      {!studentDashboard && (
        <Button
          className="remove"
          onClick={removeStudent}
          disabled={!removeButton}
          style={{ visibility: removeButton ? 'visible' : 'hidden' }}
        >
          X
        </Button>
      )}
      <Button className="name-text" disabled>
        {studentName}
      </Button>
    </div>
  );
};

export default StudentInfo;
