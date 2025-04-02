import { useEffect, useState} from "react"
import Header from "../components/Header"
import {useLocation} from 'react-router-dom'
import { modifyAdminEmail, modifyAdminName } from "../api/adminAccountApi"
import {getTeamMemberById, modifyTeamMemberEmail, modifyTeamMemberName } from "../api/teamMemberAccountApi"
import {useForm} from 'react-hook-form'
import { resetPassword } from "../api/adminApi"
import Loading from "./Loading"

export default function EditUserDetails(){
    const {register, handleSubmit} = useForm();
    const location = useLocation()
    const { accountToEdit } = location.state
    const [accountInfo, setAccountInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    const onSubmit = async(data)=>{
        //const admin = await isAdmin(accountToEdit); when is admin is fixed
        
        const admin = accountInfo.role === 'ADMIN';
        try{

            await changeName(data, admin);
            await changePassword(data, admin);
            await changeEmail(data, admin);

            await alert("User Details set");
            window.location.href="/all-users";
            
        }catch(error){
            alert(error)
        }
        
    }
    async function changeEmail(data, admin) {
        if(data.userEmail !== accountInfo.userEmail){
            if(admin){
                await changeAdminEmail(data.userEmail);
            }else{  
                await changeUserEmail(data.userEmail);
            }

        }
    }
    async function changeName(data, admin) {
        if(data.userName !== accountInfo.userName){
                if(admin){
                    await changeAdminName(data.userName)
                }else{  
                    await changeUserName(data.userName);
            }}
    }
    async function changePassword(data, admin) {
        if(data.password !== '' && data.passwordCon !== '' && data.password === data.passwordCon){
            const resetPassResponse = await resetPassword(accountToEdit, data.password)
            console.log(resetPassResponse)

        }
        if(data.password !== data.passwordCon){
            throw Error("Passwords don't match")
        }
    }
    async function changeAdminName(newUserName){
            await modifyAdminName(accountToEdit, newUserName);
    }
    async function changeAdminEmail(newUserName){
            await modifyAdminEmail(accountToEdit, newUserName);
    }
    async function changeUserName(newUserName){
            await modifyTeamMemberName(accountToEdit, newUserName);
    }
    async function changeUserEmail(newUserName){
            await modifyTeamMemberEmail(accountToEdit, newUserName);

    }


    useEffect(()=>{
        async function getData(){
            try {
                const data = await getTeamMemberById(accountToEdit);
                setAccountInfo(data)
            } catch (error) {
                console.log(error)
            }finally{
                setLoading(false)
            }
        }
        getData();
    },[accountToEdit])
    if(loading){
        return (
            <Loading/>
        )
    }


    return(
        <div className="pageContainer">
            <Header/>
            <form onSubmit={handleSubmit(onSubmit)} id = 'createAccountForm' className="pageBody">
                Change user info for: {accountInfo.userName}
                <label htmlFor="">
                   <div>
                        UserName:
                        <input type="text" defaultValue={accountInfo.userName} id="" {...register("userName",
                            {
                                required: true
                            }
                        )}/>
                   </div>
                </label>
                <label htmlFor="">
                   <div>
                        Email:
                        <input type="text" defaultValue={accountInfo.userEmail} id="" {...register("userEmail",
                            {
                                required: true
                            })}/>
                   </div>
                </label>
                <label htmlFor="">
                   <div>
                        Set a new Password:
                        <input type={'password'} id="" {...register("password")}/>
                   </div>
                </label>
                <div>
                <label htmlFor="">
                   <div>
                        Confirm new Password:
                        <input type={'password'} id="" {...register("passwordCon")}/>
                   </div>
                </label>
                </div>
                <button type="submit">Change userInfo</button>
                
            </form>
        </div>
    )
}