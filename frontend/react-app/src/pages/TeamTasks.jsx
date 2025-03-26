import { Link } from "react-router-dom";
import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/TeamTasks.css";
import TeamMember from "../components/TeamMember";
import fakeData from "../FakeData/fakeTaskData.json"
import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';
import { getTeamMembers } from "../api/teamApi";
import { useLocation } from 'react-router-dom';
import { getTeamTasks } from "../api/teamApi";


function getAssigneesNames(taskItem) {
  return taskItem.assignedMembers.map((member) => member.userName).join(", ");
}
function setUpData(results) {
  return results
  .filter((taskItem) => taskItem.status === "Not Started" || taskItem.status === "In Progress")
    .map((taskItem) => ({
      id: taskItem.taskId,
      name: taskItem.title,
      assignees: getAssigneesNames(taskItem),
      status: taskItem.status,
      priority: taskItem.priority,
      dueDate: taskItem.dueDate || "No Due Date",
      isLocked: taskItem.isLocked
    }));
}
function setUpDataCompleted(results) {
  return results
    .filter((taskItem) => taskItem.status === "Done")
    .map((taskItem) => ({
      id: taskItem.taskId,
      name: taskItem.title,
      assignees: getAssigneesNames(taskItem),
      priority: taskItem.priority,
      status: taskItem.status,
      dueDate: taskItem.dueDate || "No Due Date",
      isLocked: taskItem.isLocked
    }));
}
const headerAndAccessors = [
      {
        Header: "Task Name",
        accessor: "name",
        Cell: (original) => (
            <Link to="/view-task" state={{taskToSee: original.cell.row.values.id}}>{original.value}</Link>
          )
      },
      {
        Header: "ID",
        accessor:"id",
      },
      {
          Header: "Assignee(s)",
          accessor: "assignees",
      },
      {
        Header: "Priority",
        accessor: "priority",
      },
      {
          Header: "Status",
          accessor: "status",
      },

      {
          Header: "Due Date",
          accessor: "dueDate",
      },
      {
        Header: "Is Locked",
        accessor: "isLocked",
      }
]




const headerAndAccessorsComplete = [
  {
    Header: "Task Name",
    accessor: "name",
    Cell: (original) => (
        <Link to="/view-task" state={{taskToSee: original.cell.row.values.id}}>{original.value}</Link>
      )
  },
  {
    Header: "ID",
    accessor:"id",
  },
  {
      Header: "Assignee(s)",
      accessor: "assignees",
  },
  {
      Header: "Due Date",
      accessor: "dueDate",
  },
  {
      Header: "Date Completed",
      accessor: "dateCompteted",
  }
]
function TeamTasks(){
  const [cookies] = useCookies(['userInfo']);
  const userId = cookies.userInfo.accountId;

  const [teamMembers, setTeamMembers ] = useState([]);
  const [loadingNames, setLoadingNames] = useState(true);
  const [loadingTasks, setLoadingTasks] = useState(true);

  const location = useLocation();
  const { teamId } = location.state;
  console.log("teamId:", teamId);

  const [tasksToDo, setTasksToDo ] = useState([]);
  
  
  async function fetchData(){
      try{
          const results = await getTeamTasks(teamId);
          console.log("Team Tasks Results:", results);
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
            const data = await getTeamMembers(teamId)
            setTeamMembers(data)
        } catch (error) {
            console.log(error)
        }finally{
            setLoadingNames(false)
        }
    }
    loadAPIInfo();
      
},[])
if(loadingNames || loadingTasks){
  return (<div>Loading...</div>)
}

 

  //mock
  const isAdmin = false;

  //mock 
  const members = [
    { id: 1, name: "Adam Apple", role: "Admin" },
    { id: 2, name: "John Coder", role: "You" },
    { id: 3, name: "Jane Doe" },
    { id: 4, name: "Bob" },
    { id: 5, name: "Joe Smith" },
  ];
  


    return (
      <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
            <h2>Team 1 Tasks</h2>
            <TaskList
            dataToUse={setUpData(tasksToDo)}
            headersAndAccessors={headerAndAccessors}
            />
            <a href="/create-task"><button className="create-task-btn">Create Task</button></a>
            <h2>Completed Tasks</h2>
            <TaskList
            dataToUse={setUpDataCompleted(tasksToDo)}
            headersAndAccessors={headerAndAccessorsComplete}
            />
            

            <h2>Team Members</h2>
            <div className="team-list">
              {teamMembers.map((member) => (
                <TeamMember key={member.teamId} member={member} isAdminPage={isAdmin}/>
              ))}
            </div>

            


          </div>
        </div>

          
      );
      

}

export default TeamTasks