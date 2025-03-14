
import {useForm} from 'react-hook-form';
import React from 'react';
import '../css/CreateAccount.css';

const CreateAccount = () => {
    
    const { register, handleSubmit, formState: {errors}} = useForm();
    const onSubmit = data => {
        console.log(data);
        window.location.href="/home";
    }

    return (
        <div id="createAccountContainer">
            <form id="createAccountForm" onSubmit={handleSubmit(onSubmit)} className="form">
                    <label>Create Account</label>
                    
                    <input type="text" name="username" placeholder="Username"
                    {...register("username", {
                        required:{
                            value: true,
                            message: "Please enter a username"
                        }
                    })}
                    />

                    <input type="email" name="email" placeholder="Email"
                    {...register("email", {
                        required:{
                            value: true,
                            message: "Please enter an email"
                        }
                    })}
                    />
                        
                    <input type="password" name="password" placeholder="Password"
                    {...register("password", {
                        required:{
                            value: true,
                            message: "Please enter a password"
                        }
                    })}
                    />
                       
                     <button type="submit">Create</button>

                </form>
        </div>
    )
}

export default CreateAccount;