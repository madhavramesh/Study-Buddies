import React, { useEffect, useState } from 'react';
import './GeneralInfoClassStyle.scss';
import { Button } from 'react-bootstrap';

const axios = require('axios');

type GeneralInfoClassProps = {
  className: string;
  classNumber: string;
  classDescription: string;
  classTerm: string;
  classCode: string;
};

const IMG_WIDTH = 430;
const IMG_HEIGHT = 400;
const RANDOM_IMAGE_URL = `https://source.unsplash.com/featured/${IMG_WIDTH}x${IMG_HEIGHT}/?nature,water`;

const GeneralInfoClass: React.FC<GeneralInfoClassProps> = ({
  className,
  classNumber,
  classDescription,
  classTerm,
  classCode,
}: GeneralInfoClassProps) => {
  return (
    <div className="general-info-container">
      <div className="class-name">{className}</div>
      <div className="class-subheader">
        <div className="class-number-and-term">
          {classNumber} - {classTerm}
        </div>
        <div className="class-code">Class Code: {classCode}</div>
      </div>
      <div className="class-img">
        <img src={RANDOM_IMAGE_URL} alt="" />
      </div>
      <p className="class-description">{classDescription}</p>
    </div>
  );
};

export default GeneralInfoClass;
