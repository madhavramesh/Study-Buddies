import React, { Fragment } from 'react';
import NavBar from '../Navbar';
import './HomePage.scss';

const HomePage: React.FC = () => {
  return (
    <div className="homepage-container">
      <NavBar />
      <p className="homepage-title">Chunky Study Buddies</p>
      <p className="homepage-title homepage-info">Study Groups Done Better</p>
    </div>
  );
};

export default HomePage;
