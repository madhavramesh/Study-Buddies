import React, { useEffect, useState } from 'react';
import { Card } from 'react-bootstrap';
import './StudyGroupDisplayStyle.scss';

type StudyGroupDisplayProps = {
  groupID: string;
  studentNames: Array<string>;
};

const IMG_WIDTH = 430;
const IMG_HEIGHT = 200;
const RANDOM_IMAGE_URL = `https://source.unsplash.com/featured/${IMG_WIDTH}x${IMG_HEIGHT}/?dark, studyr`;

const StudyGroupDisplay: React.FC<StudyGroupDisplayProps> = ({
  groupID,
  studentNames,
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
        <Card.Img src={RANDOM_IMAGE_URL} alt="" />
        <div>
          <Card.ImgOverlay>
            <Card.Title className="card-header">{groupID}</Card.Title>
            <Card.Text className="student-cols">
              <div className="students-col-1">
                {studentNamesFirstHalf.map((studentName: any) => (
                  <div className={`student ${studentName}`}>{studentName}</div>
                ))}
              </div>
              <div className="students-col-2">
                {studentNamesSecondHalf.map((studentName: any) => (
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
