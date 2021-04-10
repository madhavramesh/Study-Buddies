import React, { FormEvent, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import './SigninStyle.scss';

const axios = require('axios');

const Signin: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [validAcct, setValidAcct] = useState(false);
  const [validAcctMessage, setValidAcctMessage] = useState('');

  const checkValidAccount = () => {
    const postParameters = {
      email,
      password,
    };

    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      },
    };

    console.log(`Email: ${email}`);
    console.log(`Password: ${password}`);

    axios
      .post('https://localhost:4567/validate_account', postParameters, config)
      .then((response: any) => {
        if (response.data.status) {
          setValidAcct(true);
        } else {
          setValidAcct(false);
        }
        console.log(response.data);
        setValidAcctMessage(response.data.message);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };

  return (
    <div className="signin-container">
      <div className="signin-header">
        <div className="signin-header-text">Sign in</div>
        <div className="signup">
          or&nbsp;<Link to="/signup">create an account</Link>
        </div>
      </div>
      <Form className="signin">
        <Form.Group controlId="formGroupEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control
            type="email"
            placeholder="Enter email"
            required
            isInvalid={!validAcct}
            onChange={(e: any) => setEmail(e.target.value)}
          />
          <Form.Control.Feedback type="invalid">{validAcctMessage}</Form.Control.Feedback>
        </Form.Group>

        <Form.Group controlId="formGroupPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            required
            isInvalid={!validAcct}
            onChange={(e: any) => setPassword(e.target.value)}
          />
          <Form.Control.Feedback type="invalid" />
        </Form.Group>
        <p className="forgot-password">
          <Link to="/">Forgot your password?</Link>
        </p>

        <Form.Group controlId="formGroupRememberMe">
          <Form.Check label="Remember me" />
        </Form.Group>

        <Form.Group>
          <Button variant="primary" size="sm" type="submit" onClick={checkValidAccount}>
            LOG IN
          </Button>
        </Form.Group>
      </Form>
    </div>
  );
};

export default Signin;
