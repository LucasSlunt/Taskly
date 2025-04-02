import "../css/Profile.css"
import UserInfo from "../components/UserInfo"
import Teams from "../components/Teams"
import SignOut from '../components/SignOut'
import Header from "../components/Header";
import { useCookies } from "react-cookie";
import { useEffect, useState } from "react";
import {getTeamMemberById} from '../api/teamMemberAccountApi'
import { getTeamsForMember } from "../api/teamMemberApi";

function Profile(){
    const [cookies] = useCookies(['userInfo']);
    const [userData, setUserData] = useState();
    const [teams, setTeams] = useState();
    const [loading, setLoading] = useState(true);
    //mock data

    useEffect(()=>{
        async function getUserInfo(){
            try {
                const user = await getTeamMemberById(cookies.userInfo.accountId)
                const usersTeams = await getTeamsForMember(cookies.userInfo.accountId)
                setUserData(user)
                setTeams(usersTeams)
            } catch (error) {
                
            }finally{
                setLoading(false)
            }
        }
        getUserInfo();
    },[])
    if(loading){
        return (<div>...Loading</div>)
    }
    console.log(userData)
    return (
    <div className='pageContainer'>
        <Header/>
        <div className='profile-page'>
            <UserInfo 
            userInfo = {userData}/>
            <h1>My Teams</h1>
            <div className="teams-grid">
                {teams.map((team) => (
                    <Teams team={team} key={team.teamId}/> 
                ))}
            </div> 
            <div>
            <a href="/change-password"><button className="importButton">Change Password</button></a>    
            </div> 
            <SignOut/>
        </div>
    </div>
    
    );
}

export default Profile