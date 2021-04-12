import React, { useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import { Link, useHistory } from 'react-router-dom';
import './SigninStyle.scss';

const axios = require('axios');

const Signin: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [validAcct, setValidAcct] = useState(false);
  const [validAcctMessage, setValidAcctMessage] = useState('');

  const history = useHistory();

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

    axios
      .post('http://localhost:4567/validate_account', postParameters, config)
      .then((response: any) => {
        if (response.data.status === 0) {
          setValidAcct(true);
          history.push('/dashboard');
          localStorage.setItem('user_id', response.data.id);
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

  const storeUser = () => {
    localStorage.setItem('rememberMe', rememberMe.toString());
    localStorage.setItem('email', rememberMe ? email : '');
  };

  const checkLocalStorage = () => {
    if (localStorage.getItem('rememberMe') === 'true') {
      const curEmail = localStorage.getItem('email');
      if (curEmail != null) {
        setEmail(curEmail);
      }
    }
  };

  useEffect(() => {
    checkLocalStorage();
  }, []);

  return (
    <div className="container-fluid">
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
              value={email}
              required
              isInvalid={!validAcct && validAcctMessage !== ''}
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
              isInvalid={!validAcct && validAcctMessage !== ''}
              onChange={(e: any) => setPassword(e.target.value)}
            />
            <Form.Control.Feedback type="invalid" />
          </Form.Group>
          <p className="forgot-password">
            <Link to="/">Forgot your password?</Link>
          </p>

          <Form.Group controlId="formGroupRememberMe">
            <Form.Check
              label="Remember me"
              onClick={() => {
                setRememberMe(!rememberMe);
              }}
            />
          </Form.Group>

          <Form.Group>
            <Button
              variant="primary"
              size="sm"
              onClick={() => {
                checkValidAccount();
                storeUser();
              }}
            >
              LOG IN
            </Button>
          </Form.Group>
        </Form>
      </div>
    </div>
  );
};

export default Signin;
