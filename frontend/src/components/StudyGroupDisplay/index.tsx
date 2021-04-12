import React, { useEffect, useState } from 'react';
import { Card } from 'react-bootstrap';
import './StudyGroupDisplayStyle.scss';

type StudyGroupDisplayProps = {
  groupID: string;
  studentNames: Array<string>;
  imageURL: string;
};

const StudyGroupDisplay: React.FC<StudyGroupDisplayProps> = ({
  groupID,
  studentNames,
  imageURL,
}: StudyGroupDisplayProps) => {
  const [studentNamesFirstHalf, setStudentNamesFirstHalf] = useState<Array<string>>([]);
  const [studentNamesSecondHalf, setStudentNamesSecondHalf] = useState<Array<string>>([]);

  useEffect(() => {
    const midIndex = Math.ceil(studentNames.length / 2);
    setStudentNamesFirstHalf(studentNames.splice(0, midIndex));
    setStudentNamesSecondHalf(studentNames.splice(-midIndex));
  }, [studentNames]);

  return (
    <>
      <Card className="study-group-card">
        <Card.Img src={imageURL} alt="" />
        <div>
          <Card.ImgOverlay>
            <Card.Title className="card-header">{groupID}</Card.Title>
            <Card.Text className="student-cols">
              <div className="students-col-1">
                {studentNamesFirstHalf.map((studentName: string) => (
                  <div className={`student ${studentName}`}>{studentName}</div>
                ))}
              </div>
              <div className="students-col-2">
                {studentNamesSecondHalf.map((studentName: string) => (
                  <div className={`student ${studentName}`}>{studentName}</div>
                ))}
              </div>
            </Card.Text>
          </Card.ImgOverlay>
        </div>
      </Card>
    </>
  );
};

export default StudyGroupDisplay;
