import React, { FormEvent, useEffect, useRef, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import ReCAPTCHA from 'react-google-recaptcha';
import './SignupStyle.scss';

const axios = require('axios');

const Signup: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [password2, setPassword2] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');

  const [emailMessage, setEmailMessage] = useState('');
  const [passwordMessage, setPasswordMessage] = useState('');
  const [password2Message, setPassword2Message] = useState('');
  const [firstNameMessage, setFirstNameMessage] = useState('');
  const [lastNameMessage, setLastNameMessage] = useState('');

  const recaptchaRef = useRef<ReCAPTCHA>(null);
  const [recaptchaToken, setRecaptchaToken] = useState('');
  const [recaptchaMessage, setRecaptchaMessage] = useState('');

  const [registered, setRegistered] = useState(true);

  const registerPerson = () => {
    const postParameters = {
      firstname: firstName,
      lastname: lastName,
      email,
      password,
      password2,
      token: recaptchaToken,
    };

    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };

    console.log(`First Name: ${firstName}`);
    console.log(`Last Name: ${lastName}`);
    console.log(`Email: ${email}`);
    console.log(`Password: ${password}`);

    axios
      .post('http://localhost:4567/register_account', postParameters, config)
      .then((response: any) => {
        if (response.data.success === 0) {
          setRegistered(true);
        } else {
          setRegistered(false);
        }
        setEmailMessage(response.data.email);
        setPasswordMessage(response.data.password);
        setPassword2Message(response.data.password2);
        setFirstNameMessage(response.data.first_name);
        setLastNameMessage(response.data.last_name);
        setRecaptchaMessage(response.data.token);
      })
      .catch((_: any) => {
        setRegistered(false);
      });
  };

  /*
  const validateHuman = async (): Promise<boolean> => {
    const secretKey = process.env.REACT_APP_RECAPTCHA_SECRET_KEY;

    const postParameters = {
      secret: `secret=${secretKey}&response=${recaptchaToken}`,
    };

    const config = {
      headers: {
        Accept: 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
      },
    };

    const isHuman = await axios
      .post('https://www.google.com/recaptcha/api/siteverify', postParameters, config)
      .then((response: any) => console.log(response))
      .catch((_: any) => {
        setRecaptchaMessage("Confirm that you're not a robot");
        return false;
      });

    if (recaptchaToken === '' || !isHuman) {
      setRecaptchaMessage("Confirm that you're not a robot");
      return false;
    }
    return true;
  };
  */

  const onSignUp = async () => {
    const recaptchaValue = recaptchaRef.current?.getValue();
    recaptchaRef.current?.reset();

    if (recaptchaValue != null) {
      setRecaptchaToken(recaptchaValue);
    }

    registerPerson();
  };

  return (
    <div className="container-fluid">
      <div className="signup-container">
        <div className="signup-header">
          <div className="signup-header-text">Create an account</div>
          <div className="signin">
            or&nbsp;<Link to="/signin">login</Link>
          </div>
        </div>
        <Form className="signup">
          <Form.Group controlId="formFirstName">
            <Form.Label>What&apos;s your first name?</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter your first name."
              required
              isInvalid={firstNameMessage !== ''}
              onChange={(e: any) => setFirstName(e.target.value)}
            />
            <Form.Control.Feedback type="invalid">{firstNameMessage}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formLastName">
            <Form.Label>What&apos;s your last name?</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter your last name."
              required
              isInvalid={lastNameMessage !== ''}
              onChange={(e: any) => setLastName(e.target.value)}
            />
            <Form.Control.Feedback type="invalid">{lastNameMessage}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formGroupEmail">
            <Form.Label>What&apos;s your email?</Form.Label>
            <Form.Control
              type="email"
              placeholder="Enter your email."
              required
              isInvalid={emailMessage !== ''}
              onChange={(e: any) => setEmail(e.target.value)}
            />
            <Form.Control.Feedback type="invalid">{emailMessage}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formGroupPassword">
            <Form.Label>Create a Password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Create a password."
              required
              isInvalid={passwordMessage !== ''}
              onChange={(e: any) => setPassword(e.target.value)}
            />
            <Form.Control.Feedback type="invalid">{passwordMessage}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formGroupConfirmPassword">
            <Form.Label>Create your password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Enter your password again."
              required
              isInvalid={password2Message !== ''}
              onChange={(e: any) => setPassword2(e.target.value)}
            />
            <Form.Control.Feedback type="invalid">{password2Message}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group controlId="formRECAPTCHA" className="recaptcha">
            <ReCAPTCHA
              ref={recaptchaRef}
              sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY!}
              onExpired={() => setRecaptchaToken('')}
            />
            {recaptchaMessage !== '' && <div className="recaptcha-error">{recaptchaMessage}</div>}
          </Form.Group>

          <Form.Group>
            <Button variant="primary" size="sm" onClick={onSignUp}>
              SIGN UP
            </Button>
          </Form.Group>
        </Form>
      </div>
    </div>
  );
};

export default Signup;
