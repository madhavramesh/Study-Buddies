import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import { Button, Container, Form, FormControl, Nav } from 'react-bootstrap';
import logo from '../../images/logo.svg';

const Footer = () => {
  return (
    <Navbar className="navbar" bg="black" variant="dark" sticky="top">
      <Navbar.Brand className="navbar-brand" href="/">
        <img alt="" src={logo} width="60" height="60" />
        Chunky
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

export default Footer;
