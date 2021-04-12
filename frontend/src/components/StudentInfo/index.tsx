import React from 'react';
import { Button, InputGroup } from 'react-bootstrap';
import './StudentInfoStyle.scss';
import RemoveIcon from '@material-ui/icons/Remove';
import { Remove } from '@material-ui/icons';

type StudentInfoProps = {
  studentName: string;
  removeStudent: any;
};

const StudentInfo: React.FC<StudentInfoProps> = ({
  studentName,
  removeStudent,
}: StudentInfoProps) => {
  return (
    <div className={`student ${studentName}`}>
      <Button className="remove" onClick={removeStudent}>
        X
      </Button>
      <Button className="name-text" disabled>
        {studentName}
      </Button>
    </div>
  );
};

export default StudentInfo;
