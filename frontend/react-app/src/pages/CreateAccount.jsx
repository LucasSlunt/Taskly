import React from 'react';
import '../css/CreateAccount.css';
import Header from '../components/Header';
import CreateAccountForm from '../components/CreateAccountForm';

const CreateAccount = () => {
    

    return (
        <div id="createAccountContainer">
            <Header/>
            <CreateAccountForm/>
        </div>
    )
}

export default CreateAccount;