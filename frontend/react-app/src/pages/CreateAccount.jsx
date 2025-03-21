import React from 'react';
import '../css/CreateAccount.css';
import Header from '../components/Header';
import CreateAccountForm from '../components/CreateAccountForm';

const CreateAccount = () => {
    

    return (
    <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
            <CreateAccountForm/>
        </div>
    </div>
    )
}

export default CreateAccount;