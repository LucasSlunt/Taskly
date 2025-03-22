import { useEffect } from 'react';
import {useForm} from 'react-hook-form';
import Select from 'react-select'
export default function CreateAccountForm(teams){
    const { register, handleSubmit, formState: {errors}} = useForm();
    const onSubmit = data => {
        console.log(data);
        window.location.href="/home";
    }
    return(
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
                    <select name="" id="" {...register('role')}>
                        <option value="teamMember">Team Member</option>
                        <option value="Admin">Admin</option>
                    </select>
                    <Select
                    options={teams}
                    isMulti
                    className='select'
                    />
                     <button type="submit" id ='createAccountSumbitButton'>Create</button>

                </form>
            )
}