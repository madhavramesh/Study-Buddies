import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './GeneralInfo.scss';
import { doc } from 'prettier';

type GeneralInfoProps = {
  classID: string;
};

const GeneralInfo: React.FC<GeneralInfoProps> = ({ classID }) => {
  console.log(classID);

  const getClassInfo = () => {
    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };

    axios
      .get(`http://localhost:4567/get_class_with/:${classID}`, config)
      .then((response) => {
        console.log(response.data);
        /*
        const classId = theClass.class_id;
        const className = theClass.class_name;
        const classNumber = theClass.class_number;
        const classDescription = theClass.class_description;
        const classTerm = theClass.class_term;
        const classCode = theClass.class_code;
        const classOwnerId = theClass.owner_id;
        */
        // Note: It is very important that you understand how this is set up and why it works!
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  return (
    <div>
      <h1>GENERAL INFO</h1>
      <div>asdf</div>
    </div>
  );
};

export default GeneralInfo;
