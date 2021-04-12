/* eslint-disable dot-notation */
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ClassCard from '../../components/ClassCard';

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

const TestDisplayClassesPage: React.FC = () => {
  const [classes, setClasses] = useState([]);

  useEffect(() => {
    const getAllClasses = async () => {
      const response = await axios.get('http://localhost:4567/get_all_classes', CONFIG);
      setClasses(response.data['classes']);
    };
    getAllClasses();
  }, []);

  let classCards: JSX.Element[] = [];
  if (classes.length) {
    classCards = classes.map((c) => {
      console.log(c);
      console.log(c['classId']);
      return (
        <ClassCard
          cid={c['classId']}
          name={c['className']}
          number={c['classNumber']}
          desc={c['classDescription']}
          term={c['classTerm']}
          // eslint-disable-next-line eqeqeq
          owner={c['ownerId'] == localStorage.getItem('user_id')}
        />
      );
    });
  }

  return <div>{classCards}</div>;
};

export default TestDisplayClassesPage;
