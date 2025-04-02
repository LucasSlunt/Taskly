import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/MyTasks.css";
import "../css/Header.css";
import fakeData from "../FakeData/fakeTaskData.json";
import { Link } from 'react-router-dom';
import { getAssignedTasks } from "../api/teamMemberApi";
import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';
import LockUnlockTask from "../components/LockUnlockTask";



function getAssigneesNames(taskItem) {
    return taskItem.assignedMembers.map((member) => member.userName).join(", ");
}
function mapTaskItem(taskItem) {
    return {
      name: taskItem,
      id: taskItem.taskId,
      team: taskItem.teamId,
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

function MyTasks(){
const [cookies] = useCookies(['userInfo'])
const userId = cookies.userInfo.accountId
const isAdmin =cookies.userInfo.role ==='admin';

const [tasksToDo, setTasksToDo ] = useState([]);
const [loading, setLoading] = useState(true);

async function fetchData(){
    try{
        const results = await getAssignedTasks(userId);
        console.log("API Results:", results);
        setTasksToDo(results);
    } catch (error){
        console.log("error while getting tasks: ", error);
    }finally{
        setLoading(false)
    }
}


useEffect(()=>{
    fetchData();
    console.log("Tasks To Do:", tasksToDo);
    
    
},[]);



const commonColumns = [
    {
        Header: "Task Name",
        accessor: "name",
        Cell: (original) => (
            <Link to="/view-task" state={{taskToSee: original.value, teamMembers: original.cell.row.values.assignees}}>{original.value.title}</Link>
          )
    },
    {
        Header: "Team ID",
        accessor:"team",
    },
    {
        Header: "Task ID",
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
        Header: "Status",
        accessor: "status",
    },
    {
        Header: "Priority",
        accessor: "priority",
    },
    {
        Header: "Is Locked",
        accessor: "isLocked",
        Cell: (original) => {
          const isLocked = original.value;
          return isAdmin ? (
            <LockUnlockTask initialIsLocked={isLocked} taskId={original.row.original.id} />
          ) : (
            isLocked ? '🔒' : '🔓'
          );
        },
      }
]
const headerAndAccessorsComplete = [
    ...commonColumns
]
if(loading){
    return (<div>Loading...</div>)
}
const tasksToDoData = setUpData(tasksToDo);
const tasksCompletedData = setUpDataCompleted(tasksToDo);
return (

    <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
        <div class="content-wrapper flexbox">
                
                <h1>My Tasks</h1>
                <span class ="taskBox">
                {tasksToDoData.length > 0 ? (
                    <TaskList
                    dataToUse={tasksToDoData}
                    headersAndAccessors={headerAndAccessors}
                    />
                ) : (
                    <p>No tasks to do</p>
                )}
                    

                </span>
                <a href="/create-task"><button className="create-task-btn">Create Task</button></a>
                
                <h2>My Completed Tasks</h2>
                {tasksCompletedData.length > 0 ? (
                    <TaskList
                    dataToUse={tasksCompletedData}
                    headersAndAccessors={headerAndAccessorsComplete}
                    />
                ) : (
                    <h2>No tasks completed</h2>
                )}
                
                
                
        </div>
        </div>
    </div>
    );
      

}

export default MyTasks