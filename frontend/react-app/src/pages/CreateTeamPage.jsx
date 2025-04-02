import React, { useEffect, useState } from 'react';
import '../css/CreateAccount.css';
import Header from '../components/Header';
import CreateTeamForm from '../components/CreateTeamForm';
import { getTeamMembers } from '../api/teamMemberAccountApi';
import { getAdmins } from '../api/adminApi';
import Loading from './Loading';

export default function CreatTeamPage(){
    const [loading, setLoading]= useState(true);
    const [allUsers, setTeamMemberData] = useState([]);

    function FormatUserData(users){
        let returnArr =[]
        users.map((user)=>{
            returnArr = [...returnArr, {value: user.accountId, label: user.userName, name: user.userName}]
        })
        return returnArr
    }

    useEffect(()=>{
        async function getAllUsers(){
            try {
                const teamMembers = await getTeamMembers();
                const admins = await getAdmins();
                setTeamMemberData(()=>teamMembers.concat(admins))
            } catch (error) {
                console.log(error)
            }finally{
                setLoading(false)
            }
        }
        getAllUsers();
    },[])
    if(loading){
        return (<Loading/>)
    }
    return (
    <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
            <CreateTeamForm
            users = {FormatUserData(allUsers)}
            />
        </div>
    </div>
    )
}
