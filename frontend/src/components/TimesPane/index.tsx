import React from 'react';
import { Button, Card, Form, FormControl, ListGroup, Nav } from 'react-bootstrap';
import './TimesPane.scss';
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
    if (time >= 1200) {
      am = false;
    }
    let hoursBase: string;
    if (am) {
      hoursBase = `00${Math.floor(balancedTime / 100).toString()}`;
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

  return (
    <>
      {times.map((timeText) => (
        <TimeCard timeslot={timeText} />
      ))}
    </>
  );
};

export default TimesPane;
