import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ModifiedNavBar from '../../components/ModifiedNavbar';
import './ProfilePage.scss';

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

const ProfilePage: React.FC = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [enrollments, setEnrollments] = useState('');

  const getPersonInfo = (id: string | null) => {
    axios
      .get(`http://localhost:4567/person_info/${id}`, CONFIG)
      .then((response: any) => {
        const { data } = response;
        setFirstName(data.first_name);
        setLastName(data.last_name);
        setEmail(data.email);
        setEnrollments(data.enrollments);
        console.log(response.data);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  useEffect(() => {
    const id = sessionStorage.getItem('user_id');
    console.log(id);
    getPersonInfo(id);
  }, []);

  return (
    <div id="container">
      <ModifiedNavBar username="temp name" />
      <div id="profile-content">
        <div id="info">
          <h1>
            {firstName} {lastName}
          </h1>
          <p>Email: {email}</p>
          <p>Enrollments: {enrollments}</p>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
