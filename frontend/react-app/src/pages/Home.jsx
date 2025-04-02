import {getTeamsForMember} from '../api/teamMemberApi'
import '../css/home.css';
import Header from '../components/Header'
import { useCookies } from 'react-cookie';
import { useEffect, useState } from 'react';
import {Link} from 'react-router-dom'
import fakeData from "../FakeData/fakeTaskData.json"
import TaskList from '../components/TaskList';
import { getAssignedTasks } from "../api/teamMemberApi";
import Loading from './Loading';

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


function setUpData(results) {
    return results
    .filter((taskItem) => taskItem.status !== "Done")
      .map((taskItem) => ({
        id: taskItem.taskId,
        name: taskItem,
        status: taskItem.status,
        team: taskItem.teamName,
        dueDate: taskItem.dueDate || "No Due Date", 
      }));
}





const Home = () => {


    const headerAndAccessor = [
        {
            Header: "Task Name",
            accessor: "name",
            Cell: (original) => (
                <Link to="/view-task" className = "link" state={{taskToSee: original.value, teamMembers: original.cell.row.values.assignees}}>{original.value.title}</Link>
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
            Header: "Deadline",
            accessor: 'dueDate'
        }
    ]
    


    const [cookies] = useCookies(['userInfo'])
    const userId = cookies.userInfo.accountId
    const [teams, setTeams] = useState([])
    const [loading, setLoading] = useState(true);
    const [loadingTasks, setLoadingTasks] = useState(true);
    const [tasksToDo, setTasksToDo ] = useState([]);



    async function fetchData(){
        try{
            const results = await getAssignedTasks(userId);
            console.log("API Results:", results);
            setTasksToDo(results);
        } catch (error){
            console.log("error while getting tasks: ", error);
        }finally{
            setLoadingTasks(false)
        }
    }


    useEffect(()=>{
        fetchData();
        console.log("Tasks To Do:", tasksToDo);
    },[]);
    
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
    if(loading || loadingTasks){
        return (<Loading/>)
    }
    return (


        <div className='pageContainer'>


        <Header/>
       


        <main className='pageBody'>
            <div className='rowFlexbox'>
                <div id="teamSection" className="column-box">
                    <h2>Teams</h2>
                    <hr className="section-divider" />
                
                    <div id="teamButtons" className='teamButtonContainer'>
                        {teams.map((team)=>(
                            <Link className="teamButton headerText1" to='/team-tasks' state={{ team: team}} key={team.teamId} >
                            {team.teamName}
                            </Link>
                        ))}
                    </div>
                </div>


                <div id="taskSection" className="column-box">
                        <h2>
                            <Link to="/my-tasks" className="sectionLink" title="My Tasks">Tasks</Link>  
                        </h2>
                        <hr className="section-divider" />
                        {setUpData(tasksToDo).length > 0 ? (
                        <TaskList
                        dataToUse={setUpData(tasksToDo)}
                        headersAndAccessors={headerAndAccessor}
                        />
                    ) : (
                        <p>No tasks to do</p>
                    )}
                    
                    
                </div>
            </div>
        </main>


        </div>


       
    )
};


export default Home;

