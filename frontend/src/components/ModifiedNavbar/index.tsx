import React, { useState } from 'react';
import Navbar from 'react-bootstrap/Navbar';
import { Button, DropdownButton, Dropdown, Form, Nav } from 'react-bootstrap';
import './ModifiedNavbarStyle.scss';
import { useHistory } from 'react-router-dom';
import { Avatar } from '@material-ui/core';
import chunkyLogo from '../../images/chunkylogo.png';
import ProfilePopup from '../ProfilePopup';

type ModifiedNavBarProps = {
  username: string;
};

const ModifiedNavBar: React.FC<ModifiedNavBarProps> = ({ username }) => {
  const history = useHistory();
  const [showPopup, setShowPopup] = useState(false);

  return (
    <>
      <Navbar className="modified-navbar" bg="black" variant="dark" sticky="top">
        <Navbar.Brand
          className="modified-navbar-brand"
          href="/dashboard"
          style={{ fontSize: '22px' }}
        >
          <img alt="" src={chunkyLogo} width="100" height="100" />
          Chunky Study Buddies
        </Navbar.Brand>
        <Nav className="ml-auto">
          <DropdownButton
            id="edit-profile-dropdown"
            menuAlign="right"
            variant="none"
            title={
              <div className="avatar-and-name">
                <Avatar src="/broken-image.jpg" className="avatar" />
                {username}
              </div>
            }
          >
            <Dropdown.Item
              as="button"
              onClick={() => {
                setShowPopup(!showPopup);
              }}
            >
              Profile
            </Dropdown.Item>
            <Dropdown.Divider />
            <Dropdown.Item
              as="button"
              onClick={() => {
                sessionStorage.removeItem('user_id');
                sessionStorage.removeItem('first_name');
                sessionStorage.removeItem('last_name');
                history.push('/signin');
              }}
            >
              Log Out
            </Dropdown.Item>
          </DropdownButton>
        </Nav>
      </Navbar>
      <ProfilePopup showPopup={showPopup} setShowPopup={setShowPopup} />
    </>
  );
};

export default ModifiedNavBar;
