import { useCookies } from 'react-cookie';
import Header from '../components/Header'
import {useForm} from 'react-hook-form'
import { changePassword } from '../api/teamMemberApi';
import '../css/ChangePassword.css'
import { useState } from 'react';
export default function ChangePassword(){
    const {register, handleSubmit, formState: {errors}} = useForm();
    const [cookies, removeCookie] = useCookies(['userInfo'])
    const onSubmit = async(data)=>{
        try {
            if(data.newPassword === data.conNewPassword && data.newPassword !== ''){
                const changePass = await changePassword(cookies.userInfo.accountId, data.oldPassword, data.newPassword);
                if(changePass){
                    await alert("Your password has been changed")
                    removeCookie("userInfo");
                    window.location.href="/login";
                }else{
                    await alert("Failed to change password")
                }
        }else{
            alert("THOSE PASSWORDS DON'T MATCH")
        }
    } catch (error) {
            console.log(error)
        }
        

    }


    return(
        <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
            <form onSubmit = {handleSubmit(onSubmit)} className='formContainer'>
                <div className='headerText1'>
                    Change Password
                </div>
                
                <label htmlFor="old Password">
                    Old Password
                    <div>
                        <input type='password' className='changePasswordInput' id="oldPassword" {
                            ...register('oldPassword',
                            {
                                required: true,
                                message: "You must enter the old passsword"
                            }
                        )}/>
                    </div>
                </label>
                <label htmlFor="new Password">
                    New Password
                    <div>
                        <input type='password' className='changePasswordInput' id="newPassword" {
                            ...register('newPassword',
                            {
                                required: true,
                                message: "You must enter the new passsword"
                            }
                    )}/>
                    </div>
                </label>
                <label htmlFor="confirm new Password">
                    Confirm New Password
                    <div>
                        <input type='password' className='changePasswordInput' id="conNewPassword" {
                            ...register('conNewPassword',
                            {
                                required: true,
                                message: "You must confirm new passsword"
                            }
                    )}/>
                    </div>
                </label>
                <button type='submit' className='changePassSumbitButton'>Change Password</button>
                
            </form>
            </div>
        </div>
    )

}


