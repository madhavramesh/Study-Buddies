import React from 'react';
import './LoginStyle.css';
import { Link } from 'react-router-dom';

function Login() {
    return (
        <div className='base-container'>
            <img src="https://seeklogo.com/images/S/snorlax-logo-4B47B6B547-seeklogo.com.png"></img>
            <div className='header'>Chunky Study Buddies</div>
            <div className='title'>Welcome to Chunky Study Buddies</div>
            <div className='content'>
                <div className="regular-login-form">
                    <div className='form-group'>
                        <label htmlFor='username'>Username or Email</label>
                        <input type="text" name='username' id='username' placeholder='Username or Email'/>
                    </div>
                    <div className="form-group">
                        <label htmlFor='password'>Password</label>
                        <input type="text" name='password' id='password' placeholder='Password'/>
                    </div>
                </div>
                <div className="footer">
                    <button type='button' className="sign-in">
                        Sign in
                    </button>
                </div>
                <div className='caption'>
                    New to Chunky Study Buddies?&nbsp;
                    <Link to="/register">Create Account</Link>
                </div>
            </div>
        </div>
    );
}

export default Login;