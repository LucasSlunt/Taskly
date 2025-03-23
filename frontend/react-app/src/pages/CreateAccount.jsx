import React, { useEffect, useState } from 'react';
import '../css/CreateAccount.css';
import Header from '../components/Header';
import CreateAccountForm from '../components/CreateAccountForm';
import { getTeams } from '../api/adminApi';

const CreateAccount = () => {
    const [loading, setLoading]= useState(true);
    const [teamData, setTeamData] =useState();

    function FormatTeamData(data){
        let returnArr =[]
        data.map((team)=>{
            returnArr = [...returnArr, {value: team.teamId, label: team.teamName, name: team.teamId}]
        })
        return returnArr
    }

    useEffect(()=>{
        async function getAllTeams(){
            try {
                const teams = await getTeams();
                setTeamData(teams)
                console.log(teamData)
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
            teams = {FormatTeamData(teamData)}
            />
        </div>
    </div>
    )
}

export default CreateAccount;