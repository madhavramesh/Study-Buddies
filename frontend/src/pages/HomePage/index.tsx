import React, { Fragment } from 'react';
import NavBar from '../../components/Navbar';
import './HomePage.scss';
import Footer from '../../components/Footer';

const HomePage: React.FC = () => {
  return (
    <div className="homepage-container">
      <NavBar />
      <p className="homepage-title">Chunky Study Buddies</p>
      <p className="homepage-title homepage-info">Study Groups Done Better</p>
      <Footer />
    </div>
  );
};

export default HomePage;
