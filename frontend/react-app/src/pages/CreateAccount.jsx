import React, { useEffect, useState } from 'react';
import '../css/CreateAccount.css';
import Header from '../components/Header';
import CreateAccountForm from '../components/CreateAccountForm';
import { getTeams } from '../api/adminApi';

const CreateAccount = () => {
    const [loading, setLoading]= useState(true);
    const [teamData, setTeamData] =useState();

    useEffect(()=>{
        async function getAllTeams(){
            try {
                const teams = await getTeams();
                setTeamData(teams)
            } catch (error) {
                console.log(error)
            }finally{
                setLoading(false)
            }
        }
        getAllTeams();
    },[])
    if(loading){
        return (<div>...Loading</div>)
    }
    return (
    <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
            <CreateAccountForm
            teams = {teamData}
            />
        </div>
    </div>
    )
}

export default CreateAccount;