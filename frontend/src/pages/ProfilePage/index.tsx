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
  const [email, setEmail] = useState('');

  const getPersonInfo = async (id: string | null) => {
    const response = await axios.get(`http://localhost:4567/person_info/${id}`, CONFIG);
    const { data } = response;
    console.log(data);
    setEmail(data.email);
  };

  useEffect(() => {
    const id = sessionStorage.getItem('user_id');
    console.log(id);
    getPersonInfo(id);
  }, []);

  const firstName = sessionStorage.getItem('first_name');
  const lastName = sessionStorage.getItem('last_name');
  return (
    <div style={{ overflow: 'hidden' }}>
      <ModifiedNavBar username={`${firstName} ${lastName}`} />
      <div id="profile-content">
        <div id="info">
          <h1>
            {firstName} {lastName}
          </h1>
          <p>Email: {email}</p>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
