import Header from "../components/Header";
import fakeAllTeamData from "../FakeData/fakeAllTeamsData.json"
import {useForm} from "react-hook-form"
import '../css/AdminPanel.css'
import {useNavigate} from 'react-router-dom'
import { useEffect, useState } from "react";
import { getTeams } from "../api/teamApi";

export default function AdminPanel(){
    const { register, handleSubmit} = useForm();
    const [teams, setTeams] = useState();
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const onSubmit = data => {
        console.log(data)
        //send to teamTasks with team id as prop.
        //For now because I do not have admin team tasks I will send them to team tasks
        teams.map((team)=>{
            console.log(team.teamId, data.teamId)
            if(String(team.teamId) === data.teamId){
                navigate('/team-tasks', {state: {team: team}})
            }
        })
    };
    useEffect(()=>{
        async function allTeams() {
            try {
                const response = await getTeams();
                setTeams(response);
                console.log(teams)
            } catch (error) {
                console.log(error)
            }finally{
                setLoading(false)
            }
        }
        allTeams();
    },[])
    
    if(loading){
        return(<div>...Loading</div>)
    }
    return(
        <div className='pageContainer'>
            <Header/>
            <div className='pageBody'>
            <div className="headerText1 center">Admin Controls</div>
            <div className="rowFlexbox buttonRow">
            <a href="create-account" ><button className='importButton'>Create New Account</button></a>
            <a href="/all-users"><button className="importButton">See/Edit All Users</button></a>
            <a href="/create-team"><button className="importButton">Create Team</button></a>
            </div>
                <form onSubmit={handleSubmit(onSubmit)} className="goToTeamTaskForm">
                    <select name="" id="" {...register("teamId")}>
                        {teams.map((team)=>(
                            <option value={team.teamId} key={team.teamId}>{team.teamName}</option>
                        ))}
                    </select>
                    <input type="submit" value="Go to team task page" className="button"/>

                </form>
            </div>
        </div>
    )
}