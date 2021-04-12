import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './GeneralInfo.scss';
import { doc } from 'prettier';

type GeneralInfoProps = {
  className: string;
  classNumber: string;
  classDescription: string;
  classTerm: string;
  classCode: string;
  ownerID: number;
};

const GeneralInfo: React.FC<GeneralInfoProps> = ({
  className,
  classNumber,
  classDescription,
  /*
  classTerm,
  classCode,
  ownerID,
   */
}) => {
  return (
    <div>
      <h1>{className} </h1>
      <h1>{classNumber}</h1>
      <p>{classDescription}</p>
    </div>
  );
};

export default GeneralInfo;
