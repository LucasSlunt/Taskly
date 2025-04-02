import { Link } from "react-router-dom";
import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/TeamTasks.css";
import TeamMember from "../components/TeamMember";
import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';
import { getTeamMembers as getAllTeamMembers}from "../api/teamMemberAccountApi";
import { useLocation } from 'react-router-dom';
import { getTeamTasks, getTeamMembers } from "../api/teamApi";
import LockUnlockTask from "../components/LockUnlockTask";
import DeleteTeamButton from "../components/DeleteTeamButton";
import { getAdmins } from "../api/adminApi";
import AddToTeam from "../components/AddToTeam";
import Loading from "./Loading";

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
        isLocked: !!taskItem.isLocked
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


const headerAndAccessorsComplete = [
  ...commonColumns
]
function TeamTasks(){
  const [cookies] = useCookies(['userInfo']);

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
  const updateLinkById = (id, isLocked)=>{
    setTasksToDo(tasksToDo.map((task)=>{
        if(task.taskId === id){
            task.isLocked = isLocked;
        }
        return task
    }))
}
  useEffect(()=>{
      fetchData();
      console.log("Tasks To Do:", tasksToDo);
      
      
  },[tasksToDo]);

  const isAdmin = cookies.userInfo.role ==='admin';
  const headerAndAccessors = [
    ...commonColumns,
    {
      Header: "Priority",
      accessor: "priority",
      Cell: (original) => {
        const prioirtyValue = original.value;
        let color;
        switch (prioirtyValue) {
          case "HIGH":
            color = "red";
            break;
          case "MEDIUM":
            color = "orange";
            break;
          case "LOW":
            color = "green";
            break;
          default:
            color = "black";
        }
   
        return <span style={{ color }}>{prioirtyValue}</span>;
      }

    },
    {
      Header: "Status",
      accessor: "status",
      Cell: (original) => {
          const statusValue = original.value;
          let formattedStatus;
          switch (statusValue) {
              case "InProgress":
                  formattedStatus = "In Progress";
                  break;
              case "notStarted":
                  formattedStatus = "Not Started";
                  break;
              case "done":
                  formattedStatus = "Done";
                  break;
              default:
                  formattedStatus = statusValue;
          }
          return <span>{formattedStatus}</span>;
      } 
  },
    {
      Header: "Is Locked",
      accessor: "isLocked",
      Cell: (original) => {
        const isLocked = original.value;
        return isAdmin ? (
          <LockUnlockTask initialIsLocked={isLocked} taskId={original.row.original.id} updateLinkById={updateLinkById} />
        ) : (
          isLocked ? '🔒' : '🔓'
        );
      },
    }
];

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
  return (<Loading/>)
}

 
console.log(teamLead)

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
        <h2 style={{marginBottom: '0', marginTop: '40px'}}>{team.teamName}</h2>
          {tasksToDoData.length > 0 ? (
            <TaskList
              dataToUse={tasksToDoData}
              headersAndAccessors={headerAndAccessors}
            />
          ) : (
            <p style={{marginTop: '10px', marginBottom: '30px'}}>No tasks to do</p>
          )}
            

            <a href="/create-task"><button className="create-task-btn" style={{marginBottom:'20px'}}>Create Task</button></a>
            <h2 style={{marginBottom: '0'}}>Completed Tasks</h2>
            {tasksCompletedData.length > 0 ? (
            <TaskList
              dataToUse={tasksCompletedData}
              headersAndAccessors={headerAndAccessorsComplete}
            />
          ) : (
            <h2 style={{marginTop: '10px', marginBottom: '30px'}}>No tasks completed</h2>
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