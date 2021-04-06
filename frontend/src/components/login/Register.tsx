import {Link} from "react-router-dom";
import './LoginStyle.css';
import React, {useState} from "react";

function Register() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const axios = require('axios');

    const registerPerson = () => {
        const postParameters = {
            firstname: firstName,
            lastname: lastName,
            email: email,
            password: password
        }

        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        console.log("First Name: " + firstName);
        console.log("Last Name: " + lastName);
        console.log("Email: " + email);
        console.log("Password: " + password);

        axios.post(
            "http://localhost:4567/new-account",
            postParameters,
            config
        )
            .then((response: any) => {
                if (!response.data['success']) {
                    console.log(response.data['message']);
                }
            })
            .catch((err: any) => {
                console.log(err);
            })
    }

    return (
        <div className='base-container'>
            <div className='left-side'></div>
            <div className='right-side'>
                <img src="https://seeklogo.com/images/S/snorlax-logo-4B47B6B547-seeklogo.com.png"></img>
                <div className='header'>Chunky Study Buddies</div>
                <div className='title'>Welcome to Chunky Study Buddies</div>
                <div className='content'>
                    <div className="regular-login-form">
                        <div className='form-group'>
                            <label htmlFor='first-name'>First Name</label>
                            <input type="text" name='first-name' id='first-name' placeholder='First name'
                                   onChange={(e) => setFirstName(e.target.value)}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor='last-name'>Last Name</label>
                            <input type="text" name='last-name' id='last-name' placeholder='Last Name'
                                   onChange={(e) => setLastName(e.target.value)}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor='email'>Email</label>
                            <input type="text" name='email' id='email' placeholder='Email'
                                   onChange={(e) => setEmail(e.target.value)}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor='password'>Password</label>
                            <input type="password" name='password' id='password' placeholder='Password'
                                   onChange={(e) => setPassword(e.target.value)}/>
                        </div>
                    </div>
                    <div className="footer">
                        <button type='button' className="sign-in" onClick={registerPerson}>
                            Create an account
                        </button>
                    </div>
                    <div className='caption'>
                        Already have an account?&nbsp;
                        <Link to="/signin">Sign in</Link>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Register;