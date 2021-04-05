import React from 'react';
import { GoogleLogin } from 'react-google-login';

const clientID = '553204310857-hcd2155qs2hh1sg8tiafd96b1r8ndjbd.apps.googleusercontent.com'

function Login() {
    // const onSuccess = (res) => {
    //     console.log(`[Login Success] Current User: ${res.profileObj}`);
    // }
    //
    // const onFailure = (res) => {
    //     console.log(`[Login Failed] Res: ${res}`)
    // }

    return (
        <div className='base-container'>
            <div className='header'>Login</div>
            <div className='content'>
                <div className="regular-login-form">
                    <div className='form-group'>
                        <label htmlFor='username'>Username</label>
                        <input type="text" name='username' id='username' placeholder='Username'/>
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
                <div className="hr-label">
                    <span className='hr-label-text'>or</span>
                </div>
                <div className="google-login">
                    <GoogleLogin
                        clientId={clientID}
                        buttonText="Login"
                        cookiePolicy={'single_host_origin'}
                        style={{ marginTop: '100px' }}
                        isSignedIn={true}
                    />
                </div>
            </div>
        </div>
    );
}

export default Login;