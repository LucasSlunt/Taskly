import {getTeamsForMember} from '../api/teamMemberApi'
import '../css/home.css';
import Header from '../components/Header'
import { useCookies } from 'react-cookie';
import { useEffect, useState } from 'react';
import {Link} from 'react-router-dom'
import fakeData from "../FakeData/fakeTaskData.json"
import TaskList from '../components/TaskList';
function setUpDataTasksToDo(obj){
    let ansArr = []
    fakeData.map((taskItem) =>{
   if(taskItem.status !== "done"){
    ansArr = [ ...ansArr,
        {
            id: taskItem.id,
            name: taskItem.name,
            team: taskItem.team,
            dueDate: taskItem.dueDate
            


        }]
    
   }
 }
)
if(ansArr.length > 0){
    return ansArr
}else{
    return [" "]
}
}




const Home = () => {
    const headerAndAccessor = [
        {
            Header: "Task Name",
            accessor: "name",
            Cell: (original) => (
                <Link to="/view-task" className = "link" state={{taskToSee: original.cell.row.values.id}}>{original.value}</Link>
              )
        },
        {
            Header: "ID",
            accessor: 'id'
        },
        {
            Header: "Team",
            accessor: "team"
        },
        {
            Header: "DeadLine",
            accessor: 'dueDate'
        }
    ]
    


    const [cookies] = useCookies(['userInfo'])
    const [teams, setTeams] = useState([])
    const [loading, setLoading] = useState(true);
    useEffect(()=>{
        try {
            getTeamsForMember(cookies.userInfo.accountId).then((response)=>setTeams(response))
        } catch (error) {
            console.log(error)
        }finally{
            setLoading(false)
        }
    },[])
    if(loading){
        return (<div>Loading...</div>)
    }
    return (


        <div className='pageContainer'>


        <Header/>
       


        <main className='pageBody'>
            <div id="teamSection">
                <h2>My Teams</h2>
               
                <div id="teamButtons" className='teamButtonContainer'>
                    {teams.map((team)=>(
                        <Link className="teamButton" to='/team-tasks' >
                        <button className="teamButton headerText1"key={team.teamId}>{team.teamName}</button>
                        </Link>
                        
                    ))}
                </div>
            </div>


            <div id="taskSection">
                <h2>My Tasks (Preview)</h2>
                <TaskList
                dataToUse={setUpDataTasksToDo(fakeData)}
                headersAndAccessors={headerAndAccessor}
                />
                
            </div>
        </main>


        </div>


       
    )
};


export default Home;

