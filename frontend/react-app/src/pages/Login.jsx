import React from 'react';
import LoginForm from '../components/LoginForm';
import '../login.css';
function Login(){
    return (
        <div id="login">
            <div className="loginContainer">

                <div id="siteName">
                    <img src="COSC310_GroupProject/frontend/react-app/src/icons/taskboard_checks.png" alt="logo"/>
                    <h1>Our site name</h1>
                    <p1>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum condimentum nulla pharetra, ultrices mauris ut, pharetra massa. Morbi sodales in metus in porta. Morbi vel semper ligula, at dignissim eros. Aenean sollicitudin, diam et cursus elementum, elit dui viverra leo, et commodo tortor urna et dolor</p1>
                </div>
                <div id="loginFormWrapper"> 
                    <LoginForm />
                </div>
            </div>
        </div>
    )
};

export default Login;
