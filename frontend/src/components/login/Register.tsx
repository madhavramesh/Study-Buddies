import {Link} from "react-router-dom";
import './LoginStyle.css';
import React from "react";

function Register() {
    return (
        <div className='base-container'>
            <div className='header'>Chunky Study Buddies</div>
            <div className='title'>Welcome to Chunky Study Buddies</div>
            <div className='content'>
                <div className="regular-login-form">
                    <div className='form-group'>
                        <label htmlFor='first-name'>First Name</label>
                        <input type="text" name='username' id='username' placeholder='First name'/>
                    </div>
                    <div className="form-group">
                        <label htmlFor='last-name'>Last Name</label>
                        <input type="text" name='password' id='password' placeholder='Last Name'/>
                    </div>
                    <div className="form-group">
                        <label htmlFor='email'>Email</label>
                        <input type="text" name='password' id='password' placeholder='Email'/>
                    </div>
                    <div className="form-group">
                        <label htmlFor='password'>Password</label>
                        <input type="text" name='password' id='password' placeholder='Password'/>
                    </div>
                </div>
                <div className="footer">
                    <button type='button' className="sign-in">
                        Create an account
                    </button>
                </div>
                <div className='caption'>
                    Have a Chunky Study Buddies account?&nbsp;
                    <Link to="/">Login</Link>
                </div>
            </div>
        </div>
    );
}

export default Register;