import {getTeamsForMember} from '../api/teamMemberApi'
import '../css/home.css';
import Header from '../components/Header'
import { useCookies } from 'react-cookie';
import Task from '../Task';
import { useEffect, useState } from 'react';
import {Link} from 'react-router-dom'

const testTask = [
    { taskName: "Create wireframe", taskTeam: "Team 1", dueDate: "11/05/25" },
    { taskName: "Plan things", taskTeam: "Team 2", dueDate: "10/04/25" }
]



const Home = () => {
    const [cookies] = useCookies(['userInfo'])
    const [teams, setTeams] = useState([])
    const [loading, setLoading] = useState(true);
    useEffect(()=>{
        async function loadAPIInfo() {
            try {
                const data = await getTeamsForMember(cookies.userInfo.accountId)
                setTeams(data)
            } catch (error) {
                console.log(error)
            }finally{
                setLoading(false)
            }
        }
        loadAPIInfo();
    
    },[])
    if(loading){
        return (<div>Loading...</div>)
    }
    return (


        <body>


        <Header/>
       


        <main>
            <div id="teamSection">
                <h2>My Teams</h2>
               
                <div id="teamButtons">
                    {teams.map((team)=>(
                        <Link to='/team-tasks' >
                        <button className="teamButton" key={team.teamId}>{team.teamName}</button>
                        </Link>
                        
                    ))}
                </div>
            </div>


            <div id="taskSection">
                <h2>My Tasks (Preview)</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Task Name</th>
                            <th>Team</th>
                            <th>Deadline</th>
                        </tr>
                    </thead>
                        <tbody>
                            {testTask.map((task, index) => (
                                <Task key={index} {...task}/>
                            ))}
                        </tbody>
                </table>
            </div>
        </main>


        </body>


       
    )
};


export default Home;

