import React from 'react';
import { Button } from 'react-bootstrap';
import { useHistory } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import './ErrorPageStyle.scss';

const ErrorPage = () => {
  const history = useHistory();
  return (
    <>
      <div className="header">
        <Navbar />
      </div>
      <div className="error-message">
        The page you&apos;re looking <br />
        for can&apos;t be found <br />
        <Button onClick={() => history.push('/')}>Back to home</Button>
      </div>
      <div className="footer">
        <Footer />
      </div>
    </>
  );
};

export default ErrorPage;
