import "../css/Profile.css"
import UserInfo from "../components/UserInfo"
import Teams from "../components/Teams"
import SignOut from '../components/SignOut'
import Header from "../components/Header";
import { useCookies } from "react-cookie";
import { useEffect, useState } from "react";
import {getTeamMemberById} from '../api/teamMemberAccountApi'
import { getTeamsForMember } from "../api/teamMemberApi";
import Loading from "./Loading";

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
        return (<Loading/>)
    }
    console.log(userData)
    return (
    <div className='pageContainer'>
        <Header/>
        <div className='profile-page'>
            <div className="profile-info-container">
                <UserInfo 
                    userInfo = {userData}/>
                <div className='button-group'>
                    <a href="/change-password"><button className="importButton" style={{margin: '0px', backgroundColor: '#3B3355', color: 'white', fontSize:'15px', padding: '10px', height: '35px'}}>Change Password</button></a>    
                    <div className='sign-out-btn'>
                        <SignOut style={{backgroundColor:'#3B3355'}}/>
                    </div>
                </div> 
            </div>    
            <div className="column-box" style={{width: '95%'}} >
                <h1>My Teams</h1>
                <div className="section-divider">
                    <div className="teams-grid">
                        {teams.map((team) => (
                            <Teams team={team} key={team.teamId}/> 
                        ))}
                    </div> 
                </div>
                </div>
        </div>
    </div>
    
    );
}

export default Profile