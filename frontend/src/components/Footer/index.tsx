import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import { Button, Container, Form, FormControl, Nav } from 'react-bootstrap';
import logo from '../../images/logo.svg';
import './FooterStyle.scss';

const Footer = () => {
  return (
    <Navbar className="navbar" bg="black" variant="dark" fixed="bottom">
      <Navbar.Brand className="navbar-brand" href="/">
        <img alt="" src={logo} width="60" height="60" />
        Chunky
      </Navbar.Brand>
    </Navbar>
  );
};

export default Footer;
