import React, { useState, useEffect } from 'react';
import ClassCard from '../ClassCard';

type ClassCardPaneProps = {
  classes: Array<any>;
};

const ClassCardPane: React.FC<ClassCardPaneProps> = ({ classes }: ClassCardPaneProps) => {
  let classCards: JSX.Element[] = [];
  if (classes.length) {
    classCards = classes.map((c: any) => {
      return (
        <ClassCard
          cid={c.classId}
          name={c.className}
          number={c.classNumber}
          desc={c.classDescription}
          term={c.classTerm}
          classCode={c.classCode}
          // eslint-disable-next-line eqeqeq
          owner={c.ownerId == localStorage.getItem('user_id')}
        />
      );
    });
  }

  return <>{classCards}</>;
};

export default ClassCardPane;
