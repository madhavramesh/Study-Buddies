import React, { Fragment } from 'react';
import { Button } from 'react-bootstrap';
import { useHistory } from 'react-router';
import './HomePage.scss';
import NavBar from '../../components/Navbar';
import Footer from '../../components/Footer';

const HomePage: React.FC = () => {
  const history = useHistory();

  const redirectAbout = () => {
    history.push('/about');
  };

  const redirectSignup = () => {
    history.push('/signup');
  };

  return (
    <div style={{ overflow: 'hidden' }}>
      <NavBar />
      <div className="homepage-title-container">
        <p className="homepage-title">Study Groups</p>
        <p className="homepage-title homepage-title-2">Done Better</p>
        <div className="homepage-title-down-arrow" />
      </div>
      <div className="homepage-info-container">
        <p className="homepage-info">
          Study productively with like-minded people. Meet with people who align with your
          circumstances.
        </p>
        <img
          src="https://svg-clipart.com/svg/blue/hWBOREj-blue-book-vector.svg"
          alt="Blue Book SVG icons"
          className="homepage-info-image"
        />
        <Button className="homepage-info-button" onClick={redirectAbout}>
          How It Works
        </Button>
      </div>
      <div className="homepage-signup-container">
        <p className="homepage-signup-shill">
          Improve your grades, guaranteed&#42;. No credit card needed.
        </p>
        <Button className="homepage-signup-button" onClick={redirectSignup}>
          Join Now
        </Button>
        <p className="homepage-signup-addendum">&#42;this is possibly cap</p>
      </div>
      <Footer />
    </div>
  );
};

export default HomePage;
