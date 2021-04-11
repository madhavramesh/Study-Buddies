import React, { useState } from 'react';
import { Button, Card, Form, FormControl, ListGroup, Nav, Table } from 'react-bootstrap';
import './TimesPane.scss';
import { blue } from '@material-ui/core/colors';
import TimeCard from '../TimeCard';

// TimeProps consumes a number which is either is a multiple is a factor of 60.
// this indicates the length of
type TimeProps = {
  slotLength: number;
};

const TimesPane: React.FC<TimeProps> = ({ slotLength }) => {
  const convertToTime = (time: number): string => {
    const balancedTime: number = time % 2400;
    let am = true;
    if (balancedTime >= 1200) {
      am = false;
    }
    let hoursBase: string;
    if (am) {
      if (Math.floor(balancedTime / 100) === 0) {
        hoursBase = '0012';
      } else {
        hoursBase = `00${Math.floor(balancedTime / 100).toString()}`;
      }
    } else {
      hoursBase = `00${(Math.floor(balancedTime / 100) - 12).toString()}`;
    }

    const minutesBase = `00${(time % 100).toString()}`;

    const hours: string = hoursBase.substr(hoursBase.length - 2, hoursBase.length - 1);
    const minutes: string = minutesBase.substr(minutesBase.length - 2, minutesBase.length - 1);

    if (am) {
      return `${hours}:${minutes}AM`;
    }
    return `${hours}:${minutes}PM`;
  };
  const times: Array<string> = [];
  let curTime = 0;
  let timeToAdd = '';
  // Adds the times into times
  while (curTime < 2400) {
    if ((curTime + slotLength) % 100 === 60) {
      timeToAdd = `${convertToTime(curTime)} - ${convertToTime(curTime + slotLength + 40)}`;
    } else {
      timeToAdd = `${convertToTime(curTime)} - ${convertToTime(curTime + slotLength)}`;
    }
    times.push(timeToAdd);
    curTime += slotLength;
    if (curTime % 100 === 60) {
      curTime = curTime + 100 - (curTime % 100);
    }
  }

  const days: Array<string> = [
    'Monday',
    'Tuesday',
    'Wednesday',
    'Thursday',
    'Friday',
    'Saturday',
    'Sunday',
  ];

  // console.log(defaultTimes);
  const timesFree = {
    Monday: 0,
    Tuesday: 0,
    Wednesday: 0,
    Thursday: 0,
    Friday: 0,
    Saturday: 0,
    Sunday: 0,
  };

  const singleDayBooleans: Array<boolean> = times.map(() => false);
  const initialSelections: Array<Array<boolean>> = days.map(() => [...singleDayBooleans]);
  const [selectedTimes, setSelectedTimes] = useState(initialSelections);

  function selectTimes(e: any) {
    e.preventDefault();

    const { cellIndex } = e.target;
    const { rowIndex } = e.target.parentElement;

    console.log(e.target.style);
    console.log(cellIndex);
    console.log(rowIndex);
    // There are 7 cell index, they coincide with the first index of the 2d array
    const newSelectedTimes = [...selectedTimes];
    newSelectedTimes[cellIndex][rowIndex - 1] = !newSelectedTimes[cellIndex][rowIndex - 1];
    setSelectedTimes(newSelectedTimes);
    console.log('Selected Times');
    console.log(selectedTimes);
  }

  return (
    <>
      <Table striped bordered hover>
        <thead>
          <tr>
            {days.map((day) => (
              <th>{day}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {times.map((timeText) => (
            <tr>
              {days.map(() => (
                <td
                  role="presentation"
                  className={
                    selectedTimes[this.cellIndex][this.parentElement.rowIndex]
                      ? 'selected-time'
                      : 'unselected-time'
                  }
                  onClick={selectTimes}
                  onKeyDown={selectTimes}
                >
                  {' '}
                  {timeText}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </Table>
    </>
  );
};

export default TimesPane;
