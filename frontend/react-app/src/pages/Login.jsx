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
                    <p>Streamline teamwork with smart task management. Assign, prioritize, and track tasks across multiple teams. Collaborate seamlessly, stay organized with automated notifications, and keep projects moving forward effortlessly.</p>
                </div>
                <div id="loginFormWrapper"> 
                    <LoginForm />
                </div>
            </div>
        </div>
    )
};

export default Login;
