import React from 'react';
import LoginForm from '../components/LoginForm';
import '../login.css';
import logo from '../icons/taskboard_checks.png';
function Login(){
    return (
        <div id="login">
            <div className="loginContainer">

                <div id="siteName">
                    <div id="logoBox">
                        <img src={logo} alt="logo"/>
                        <h1>Taskly</h1>
                    </div>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum condimentum nulla pharetra, ultrices mauris ut, pharetra massa. Morbi sodales in metus in porta. Morbi vel semper ligula, at dignissim eros. Aenean sollicitudin, diam et cursus elementum, elit dui viverra leo, et commodo tortor urna et dolor</p>
                </div>
                <div id="loginFormWrapper"> 
                    <LoginForm />
                </div>
            </div>
        </div>
    )
};

export default Login;
