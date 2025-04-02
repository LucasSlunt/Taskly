import { Link } from "react-router-dom";
import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/TeamTasks.css";
import TeamMember from "../components/TeamMember";
import fakeData from "../FakeData/fakeTaskData.json"
import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';
import { getTeamMembers } from "../api/teamApi";
import { getTeamMembers as getAllTeamMembers}from "../api/teamMemberAccountApi";
import { useLocation } from 'react-router-dom';
import { getTeamTasks } from "../api/teamApi";
import DeleteTeamButton from "../components/DeleteTeamButton";
import { getAdmins } from "../api/adminApi";
import AddToTeam from "../components/AddToTeam";

function getAssigneesNames(taskItem) {
  return taskItem.assignedMembers.map((member) => member.userName).join(", ");
}

function mapTaskItem(taskItem) {
  return {
    name: taskItem,
    id: taskItem.taskId,
    assignees: getAssigneesNames(taskItem),
    dueDate: taskItem.dueDate || "No Due Date",
  };
}
function setUpData(results) {
  return results
  .filter((taskItem) => taskItem.status !== "Done")
    .map((taskItem) => {
      const baseItem = mapTaskItem(taskItem);
      return{
        ...baseItem,
        status: taskItem.status,
        priority: taskItem.priority,
        isLocked: taskItem.isLocked.toString()
      };
      
    });
}
function setUpDataCompleted(results) {
  return results
    .filter((taskItem) => taskItem.status === "Done")
    .map((taskItem) => {
      const baseItem = mapTaskItem(taskItem);
      return{
        ...baseItem,
        dateCompleted: taskItem.dateCompleted,
      };
    });
}

const commonColumns= [
  {
    Header: "Task Name",
    accessor: "name",
    Cell: (original) => (
        <Link to="/view-task" state={{taskToSee: original.value, teamMembers: original.cell.row.values.assignees}}>{original.value.title}</Link>
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
]
const headerAndAccessors = [
    ...commonColumns,
    {
      Header: "Priority",
      accessor: "priority",
    },
    {
        Header: "Status",
        accessor: "status",
    },
    {
      Header: "Is Locked",
      accessor: "isLocked",
    }
]

const headerAndAccessorsComplete = [
  ...commonColumns
]
function TeamTasks(){
  const [cookies] = useCookies(['userInfo']);
  const userId = cookies.userInfo.accountId;

  const [teamMembers, setTeamMembers ] = useState([]);
  const [loadingNames, setLoadingNames] = useState(true);
  const [loadingTasks, setLoadingTasks] = useState(true);
  const [allUsersLoading, setallUsersLoading] = useState(true)

  const location = useLocation();
  const { team } = location.state;
  const teamId = team.teamId;
  console.log("teamId:", teamId);

  const [tasksToDo, setTasksToDo ] = useState([]);
  const [allUsers, setAllUsers] = useState();
  const [teamLead, setTeamLead] = useState(team.teamLeadId)
  
  async function fetchData(){
      try{
        if(cookies.userInfo.role === 'admin'){
          getAllUsers();
        }
          const results = await getTeamTasks(teamId);
          console.log("Team Tasks Results:", results);
          setTasksToDo(results);
      } catch (error){
          console.log("error while getting tasks: ", error);
      }finally{
          setLoadingTasks(false)
      }
  }

  async function getAllUsers() {
    try {
      const adminResponse = await getAdmins();
      const teamMemberResposne = await getAllTeamMembers()
      setAllUsers(adminResponse.concat(teamMemberResposne))
      console.log(teamMemberResposne)
    } catch (error) {
      await alert("FAILED TO LOAD CONTACT NETWORK ADMIN")
    }finally{
      setallUsersLoading(false)
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
      
},[teamLead])
if(loadingNames || loadingTasks){
  return (<div>Loading...</div>)
}

 
console.log(teamLead)
  //mock
  const isAdmin = cookies.userInfo.role ==='admin';

  //mock 
  const members = [
    { id: 1, name: "Adam Apple", role: "Admin" },
    { id: 2, name: "John Coder", role: "You" },
    { id: 3, name: "Jane Doe" },
    { id: 4, name: "Bob" },
    { id: 5, name: "Joe Smith" },
  ];
  
  const tasksToDoData = setUpData(tasksToDo);
  const tasksCompletedData = setUpDataCompleted(tasksToDo);

    return (


      <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
        <h2>{team.teamName}</h2>
          {tasksToDoData.length > 0 ? (
            <TaskList
              dataToUse={tasksToDoData}
              headersAndAccessors={headerAndAccessors}
            />
          ) : (
            <p>No tasks to do</p>
          )}
            

            <a href="/create-task"><button className="create-task-btn">Create Task</button></a>
            <h2>Completed Tasks</h2>
            {tasksCompletedData.length > 0 ? (
            <TaskList
              dataToUse={tasksCompletedData}
              headersAndAccessors={headerAndAccessorsComplete}
            />
          ) : (
            <h2>No tasks completed</h2>
          )}
          
            

            <h2>Team Members</h2>
            <div className="team-list">
              {teamMembers.map((member) => (
                <TeamMember key={member.teamId} member={member} teamLeadId = {teamLead}
                 setTeamMembers = {setTeamMembers}
                teamId={teamId}
                isAdminPage={isAdmin}
                teamMembers={teamMembers}
                setTeamLead = {setTeamLead}
                />
              ))}
            </div>
            {cookies.userInfo.role === 'admin'&&allUsersLoading&&(<div>..Loading</div>)}
            {cookies.userInfo.role === 'admin'&& !allUsersLoading &&
            (
                <div style={{marginTop: '30px'}}>
                  <h2>Add New Team Member</h2>
                  <div>
                    <AddToTeam
                    teamId={teamId}
                    allTeamMembers={allUsers}
                    currentMembers = {
                      (teamMembers.map((member)=>member.accountId))
                    }
                    setTeamMembers = {setTeamMembers}
                    />
                  </div>
                <div>
                <DeleteTeamButton
              teamId={teamId}
              />
                </div>
              </div>
            )
            }

            


          </div>
        </div>

          
      );
      

}

export default TeamTasks