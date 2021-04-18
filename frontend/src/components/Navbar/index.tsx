import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import { Nav } from 'react-bootstrap';
import logo from '../../images/logo.svg';
import './NavbarStyle.scss';
import chunkyLogo from '../../images/chunkylogo.png';

const NavBar: React.FC = () => {
  return (
    <Navbar className="navbar-home-page" bg="black" variant="dark" sticky="top">
      <Navbar.Brand className="navbar-home-page-brand" href="/" style={{ fontSize: '22px' }}>
        <img alt="" src={chunkyLogo} width="100" height="100" />
        Chunky Study Buddies
      </Navbar.Brand>
      <Nav className="ml-auto">
        <Nav.Link href="/">Home</Nav.Link>
        <Nav.Link href="/about">About</Nav.Link>
        <Nav.Link className="separator">|</Nav.Link>
        <Nav.Link href="/signup">Sign up</Nav.Link>
        <Nav.Link href="/signin">Log in</Nav.Link>
      </Nav>
    </Navbar>
  );
};

export default NavBar;
