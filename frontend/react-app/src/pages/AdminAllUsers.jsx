import UserTable from '../components/UserTable'
import Header from '../components/Header'
import '../css/AdminAllUsers.css'
import {useState, useEffect} from 'react'
import { getTeams } from '../api/adminApi';


function AdminAllUsers(){
    const [teamData, setTeamData] = useState();
    const [loading, setLoading] = useState(true);
    useEffect(()=>{
        async function getTeamData() {
            try {
                const teams = await getTeams();
                setTeamData(teams);
            } catch (error) {
                alert(error)
            }finally{
                setLoading(false);
            }
        }
        getTeamData();
        
    },[])
    if(loading){
        return(<div>...loading</div>)
    }
    return(
        <div className='pageContainer'>
            <Header/>
            <div className='pageBody'>
                <UserTable
                teams={teamData}
                />
                <a href="create-account"><button >Create New Account</button></a>
            </div>

        </div>
    )
}
export default AdminAllUsers
/*
 <UserTable
                teamMember = {AllTeams[0].members}
                />
*/