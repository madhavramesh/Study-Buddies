import React from 'react';
import Navbar from 'react-bootstrap/Navbar';
import { Button, DropdownButton, Dropdown, Form, Nav } from 'react-bootstrap';
import './ModifiedNavbarStyle.scss';
import { useHistory } from 'react-router-dom';
import logo from '../../images/logo.svg';

type ModifiedNavBarProps = {
  username: string;
};

const ModifiedNavBar: React.FC<ModifiedNavBarProps> = ({ username }) => {
  const history = useHistory();

  return (
    <Navbar className="modified-navbar" bg="black" variant="dark" sticky="top">
      <Navbar.Brand className="modified-navbar-brand" href="/">
        <img alt="" src={logo} width="60" height="60" />
        Chunky Study Buddies
      </Navbar.Brand>
      <Nav className="ml-auto">
        <DropdownButton
          id="edit-profile-dropdown"
          menuAlign="right"
          variant="none"
          title={username}
        >
          <Dropdown.Item
            as="button"
            onClick={() => {
              history.push('/profile');
            }}
          >
            Profile
          </Dropdown.Item>
          <Dropdown.Divider />
          <Dropdown.Item
            as="button"
            onClick={() => {
              history.push('/signin');
            }}
          >
            Log Out
          </Dropdown.Item>
        </DropdownButton>
      </Nav>
    </Navbar>
  );
};

export default ModifiedNavBar;
