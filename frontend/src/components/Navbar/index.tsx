import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import { Button, Container, Form, FormControl, Nav } from 'react-bootstrap';
import logo from '../../images/logo.svg';
import './NavbarElements.scss';

const NavBar: React.FC = () => {
  return (
    <>
      <Navbar className="navbar" bg="dark" variant="dark" sticky="top">
        <Navbar.Brand href="/">
          <img alt="" src={logo} width="30" height="30" />
          Chunky Study Buddies
        </Navbar.Brand>
        <Nav className="mr-auto">
          <Nav.Link href="/">Home</Nav.Link>
          <Nav.Link href="/about">About</Nav.Link>
        </Nav>
        <Form inline>
          <FormControl type="text" placeholder="Search" className="mr-sm-2" />
          <Button variant="outline-info">Search</Button>
        </Form>
      </Navbar>
    </>
  );
};

export default NavBar;
