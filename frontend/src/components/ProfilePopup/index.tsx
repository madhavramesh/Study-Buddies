import axios from 'axios';
import React, { useState, useEffect, useRef } from 'react';
import './ProfilePopup.scss';
import { Avatar } from '@material-ui/core';
import { Form, Button, Modal } from 'react-bootstrap';
import { useHistory } from 'react-router';

type ClassProps = {
  showPopup: boolean;
  setShowPopup: any;
};

const CONFIG = {
  headers: {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  },
};

const ProfilePopup: React.FC<ClassProps> = ({ showPopup, setShowPopup }) => {
  const history = useHistory();
  /**
   * Hook that alerts clicks outside of the passed ref.
   * Code gracefully plundered from https://stackoverflow.com/questions/32553158/detect-click-outside-react-component.
   */
  function useOutsideAlerter(ref: any) {
    useEffect(() => {
      /**
       * Alert if clicked on outside of element
       */
      function handleClickOutside(event: any) {
        if (ref.current && !ref.current.contains(event.target)) {
          setShowPopup(false);
        }
      }

      // Bind the event listener
      document.addEventListener('mousedown', handleClickOutside);
      return () => {
        // Unbind the event listener on clean up
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, [ref]);
  }

  const wrapperRef = useRef<any>(null);
  useOutsideAlerter(wrapperRef);
  const personInfo = useRef<any>({});

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');

  const [confirmPasswordMessage, setConfirmPasswordMessage] = useState('');
  const [newPasswordMessage, setNewPasswordMessage] = useState('');
  const [validated, setValidated] = useState(false);

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [confirmDeletePassword, setConfirmDeletePassword] = useState('');
  const [confirmDeletePasswordMessage, setConfirmDeletePasswordMessage] = useState('');
  const [deleteAccountStatus, setDeleteAccountStatus] = useState(false);

  useEffect(() => {
    const getPersonInfo = async () => {
      const response = await axios.get(
        `http://localhost:4567/person_info/${sessionStorage.getItem('user_id')}`,
        CONFIG
      );
      personInfo.current = response.data;
      setFirstName(response.data.first_name);
      setLastName(response.data.last_name);
      setEmail(response.data.email);
    };
    getPersonInfo();
  }, []);

  const changePassword = async () => {
    const postParameters = {
      id: sessionStorage.getItem('user_id'),
      confirm_password: confirmPassword,
      new_password: newPassword,
      confirm_new_password: confirmNewPassword,
    };

    const response = await axios.post(
      'http://localhost:4567/change_password',
      postParameters,
      CONFIG
    );
    const { data } = response;
    setConfirmPasswordMessage(data.confirm_password);
    setNewPasswordMessage(data.new_password);
    if (data.status === 0) {
      setValidated(true);
    }
  };

  const deleteAccount: any = async () => {
    const postParameters = {
      id: sessionStorage.getItem('user_id'),
      password: confirmDeletePassword,
    };

    const response = await axios.post(
      'http://localhost:4567/delete_account',
      postParameters,
      CONFIG
    );
    setConfirmDeletePasswordMessage(response.data.message);
    if (response.data.status === 0) {
      setDeleteAccountStatus(true);
      setTimeout(() => history.push('/'), 2250);
    }
  };

  console.log(personInfo);
  console.log(showPopup);

  return (
    <>
      <div ref={wrapperRef} className={`profile-container ${showPopup ? 'selected' : 'dismiss'}`}>
        <div className="container-header">
          {firstName} {lastName} [{sessionStorage.getItem('user_id')}]
        </div>
        <Avatar src="/broken-image.jpg" className="avatar-icon">
          {firstName?.charAt(0)} {lastName?.charAt(0)}
        </Avatar>
        <Form className="change-info" noValidate validated={validated}>
          <Form.Group>
            <Form.Label>First Name</Form.Label>
            <Form.Control
              size="sm"
              className="change-info-control-off"
              type="text"
              value={firstName}
              disabled
            />
          </Form.Group>
          <Form.Group>
            <Form.Label>Last Name</Form.Label>
            <Form.Control
              size="sm"
              className="change-info-control-off"
              type="text"
              value={lastName}
              disabled
            />
          </Form.Group>
          <Form.Group>
            <Form.Label>Email</Form.Label>
            <Form.Control
              size="sm"
              className="change-info-control-off"
              type="text"
              value={email}
              disabled
            />
          </Form.Group>
          <hr
            style={{
              border: 'none',
              borderRadius: '1px',
              height: '2.5px',
              backgroundColor: '#c0c2c5',
              margin: '20px 0 10px 0',
            }}
          />
          <Form.Group>
            <Form.Label>Verify Password</Form.Label>
            <Form.Control
              size="sm"
              className="change-info-control"
              type="password"
              value={confirmPassword}
              isInvalid={confirmPasswordMessage !== ''}
              onChange={(e: any) => setConfirmPassword(e.target.value)}
            />
            <Form.Control.Feedback type="invalid">{confirmPasswordMessage}</Form.Control.Feedback>
          </Form.Group>
          <Form.Group>
            <Form.Label>New Password</Form.Label>
            <Form.Control
              size="sm"
              className="change-info-control"
              type="password"
              value={newPassword}
              isInvalid={newPasswordMessage !== ''}
              onChange={(e: any) => setNewPassword(e.target.value)}
            />
            <Form.Control.Feedback type="invalid">{newPasswordMessage}</Form.Control.Feedback>
          </Form.Group>
          <Form.Group>
            <Form.Label>Confirm New Password</Form.Label>
            <Form.Control
              size="sm"
              className="change-info-control"
              type="password"
              value={confirmNewPassword}
              isInvalid={newPasswordMessage !== ''}
              onChange={(e: any) => setConfirmNewPassword(e.target.value)}
            />
            <Form.Control.Feedback style={{ color: 'lightgreen' }}>
              Password successfully updated!
            </Form.Control.Feedback>
            <Form.Control.Feedback type="invalid">{newPasswordMessage}</Form.Control.Feedback>
          </Form.Group>
          <Form.Group>
            <Button className="change-info-submit" onClick={changePassword}>
              CHANGE PASSWORD
            </Button>
          </Form.Group>
          <Form.Group>
            <Button className="change-info-delete" onClick={() => setShowDeleteModal(true)}>
              DELETE ACCOUNT
            </Button>
          </Form.Group>
        </Form>
      </div>
      <Modal
        className="bring-to-front"
        size="lg"
        show={showDeleteModal}
        onHide={() => setShowDeleteModal(false)}
        centered
      >
        <Modal.Header>
          <b>Are you sure you want to delete your account?</b>
        </Modal.Header>
        <Modal.Body>
          <Form noValidate validated={deleteAccountStatus}>
            <Form.Group>
              <Form.Label>Confirm your password again:</Form.Label>
              <Form.Control
                size="lg"
                type="password"
                value={confirmDeletePassword}
                isInvalid={confirmDeletePasswordMessage === 'Invalid password!'}
                onChange={(e: any) => setConfirmDeletePassword(e.target.value)}
              />
              <Form.Control.Feedback type="invalid">
                {confirmDeletePasswordMessage}
              </Form.Control.Feedback>
              <Form.Control.Feedback style={{ color: 'green' }}>
                {confirmDeletePasswordMessage} rip ur account 🙀
              </Form.Control.Feedback>
            </Form.Group>
            <Form.Group>
              <Button className="change-info-delete" onClick={deleteAccount}>
                DELETE ACCOUNT 😖
              </Button>
            </Form.Group>
          </Form>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default ProfilePopup;
