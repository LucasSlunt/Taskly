import {useForm} from 'react-hook-form'
import {login} from '../api/authInfo'
import useSignIn from 'react-auth-kit/hooks/useSignIn';
function LoginForm(){
    const { register, handleSubmit, formState: {errors}} = useForm();
    const signIn = useSignIn();
    const onSubmit = async(data)=> {
        try {
            const responseLogin = await login(data.username, data.password);
            if(responseLogin === null){
                alert("Failed Login")
            }else{
                signIn({
                    auth:{
                        //sample token until we implement tokens for user auth
                        token: 'eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTc0MTg0ODI1MywiaWF0IjoxNzQxODQ4MjUzfQ.UWOWdqHIfEy9-PT3tZoeosL8hofXY3vPB0w72o-4_wo',
                        type: 'Bearer'
                    },
                    userState:{
                        id: responseLogin.id,
                        name: responseLogin.name,
                        role: (responseLogin.isAdmin ? "admin":"teamMember")
                    }
                })
                window.location.href="/home";
            }
        } catch (error) {

        } 
    };
    return(
        <form onSubmit = {handleSubmit(onSubmit)} id="loginForm" >
                    <label>Log in</label>
                    
                    <input type="text" name="username" placeholder="Username" {...register("username", { 
                        required:{
                            value: true,
                            message: 'Please enter a Username Name'
                        }
                        })}/>
                    <input type="text" name="password" placeholder="Password" {...register("password", { 
                        required:{
                            value: true,
                            message: 'Please set a Password'
                        }
                        })}/>

                    
                    <button type="submit" >Login</button>
                
                </form>
    )

}
export default LoginForm