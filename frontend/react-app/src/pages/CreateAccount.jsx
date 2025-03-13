import React from 'react';
import '../css/CreateAccount.css';

const CreateAccount = () => {
    return (
        <div id="createAccountContainer">
            <form id="createAccountForm">
                    <label>Create Account</label>
                    
                    <input type="text" name="username" placeholder="Username"/>

                    <input type="email" name="email" placeholder="Email"/>
                        
                    <input type="password" name="password" placeholder="Password"/>
                       
                     <button type="submit">Create</button>

                </form>
        </div>
    )
}

export default CreateAccount;