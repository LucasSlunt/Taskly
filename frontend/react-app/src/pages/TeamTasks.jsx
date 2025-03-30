import { Link } from "react-router-dom";
import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/TeamTasks.css";
import TeamMember from "../components/TeamMember";
import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getTeamTasks, getTeamMembers } from "../api/teamApi";
import {lockTask, unlockTask} from "../api/adminApi";
import LockUnlockTask from "../components/LockUnlockTask";

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
  ...commonColumns,
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

  
  const [lockedTasks, setLockedTasks] = useState({});
  const handleLockUnlock = async (id, isLocked) => {
    console.log("id " + id);
    console.log("is locked" + isLocked);
    try {
      if (isLocked) {
        console.log("task is locked");
        const unlock = await unlockTask(id);
        if (unlock) {
          console.log(`Task unlocked successfully.`);
          setLockedTasks((prevLockedTasks) => ({
            ...prevLockedTasks,
            [id]: false,
          }));
        }
      } else {
        console.log("task is unlocked");
        const lock = await lockTask(id);
        if (lock) {
          console.log(`Task locked successfully.`);
          setLockedTasks((prevLockedTasks) => ({
            ...prevLockedTasks,
            [id]: true,
          }));
        }
      }
    } catch (error) {
      console.log(`error`);
    }
  };

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
      Cell: (original) => {
        const taskId = original.row.original.id;
        const isLocked = original.value;
        return <LockUnlockTask initialIsLocked={isLocked} taskId={taskId} />;
      }
        
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
  
  const tasksToDoData = setUpData(tasksToDo);
  const tasksCompletedData = setUpDataCompleted(tasksToDo);

    return (


      <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
        <h2>Team 1 Tasks</h2>
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
                <TeamMember key={member.teamId} member={member} isAdminPage={isAdmin}/>
              ))}
            </div>

            


          </div>
        </div>

          
      );
      

}

export default TeamTasks