import React, { useState } from 'react';
import './LoginStyle.css';
import { Link } from 'react-router-dom';

const axios = require('axios');

function Login() {
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
      .post('https://localhost:4567/validate-account', postParameters, config)
      .then((response: any) => {
        if (response.data.valid) {
          setValidAcct(true);
        } else {
          setValidAcct(false);
        }
        setValidAcctMessage(response.data.message);
      })
      .catch((err: any) => {
        console.log(err);
      });
  };
  return (
    <div className="base-container">
      <div className="left-side" />
      <div className="right-side">
        <img src="https://seeklogo.com/images/S/snorlax-logo-4B47B6B547-seeklogo.com.png" alt="" />
        <div className="header">Chunky Study Buddies</div>
        <div className="title">Welcome to Chunky Study Buddies</div>
        <div className="content">
          <div className="regular-login-form">
            <div className="form-group">
              <label htmlFor="username">
                Username or Email
                <br />
                <input
                  type="text"
                  name="username"
                  id="username"
                  placeholder="Username or Email"
                  onChange={(e) => setEmail(e.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label htmlFor="password">
                Password
                <br />
                <input
                  type="password"
                  name="password"
                  id="password"
                  placeholder="Password"
                  onChange={(e) => setPassword(e.target.value)}
                />
              </label>
            </div>
          </div>
          <div className="footer">
            <button type="button" className="sign-in" onClick={checkValidAccount}>
              Sign in
            </button>
          </div>
          <div className="error-wrapper">
            {!validAcct && <div className="error">{validAcctMessage}</div>}
          </div>
          <div className="caption">
            Don&#39;t have an account yet?&nbsp;
            <Link to="/signup">Sign up</Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
