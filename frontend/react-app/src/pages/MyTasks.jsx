import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/MyTasks.css"
import fakeData from "../FakeData/fakeTaskData.json"
import { Link } from 'react-router-dom';
import { getAssignedTasks } from "../api/teamMemberApi";
import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';



function getAssigneesNames(taskItem) {
    return taskItem.assignedMembers.map((member) => member.userName).join(", ");
}

function setUpData(results) {
    return results
    .filter((taskItem) => taskItem.status !== "Done")
      .map((taskItem) => ({
        id: taskItem.taskId,
        name: taskItem,
        team: taskItem.teamId,
        assignees: getAssigneesNames(taskItem),
        status: taskItem.status,
        priority: taskItem.priority,
        dueDate: taskItem.dueDate || "No Due Date",
        isLocked: taskItem.isLocked.toString()
      }));
}

function setUpDataCompleted(results) {
    return results
      .filter((taskItem) => taskItem.status === "Done")
      .map((taskItem) => ({
        id: taskItem.taskId,
        name: taskItem,
        assignees: getAssigneesNames(taskItem),
        priority: taskItem.priority,
        status: taskItem.status,
        dueDate: taskItem.dueDate || "No Due Date",
        dateCompleted: taskItem.dateCompleted,
        isLocked: taskItem.isLocked.toString()
      }));
  }

function MyTasks(){
const [cookies] = useCookies(['userInfo'])
const userId = cookies.userInfo.accountId

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

useEffect(() => {
    
    console.log("Tasks To Do (after state update):", tasksToDo);
}, [tasksToDo]);

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
    }
]
const headerAndAccessorsComplete = [
    ...commonColumns,
    {
        Header: "Date Completed",
        accessor: "dateCompteted",
    }
]
if(loading){
    return (<div>Loading...</div>)
}
return (

    <div className='pageContainer'>
        <Header/>
        <div className='pageBody'>
        <div class="content-wrapper flexbox">
                
                <h1>My Tasks</h1>
                <span class ="taskBox">
                {setUpData(tasksToDo).length > 0 ? (
                    <TaskList
                    dataToUse={setUpData(tasksToDo)}
                    headersAndAccessors={headerAndAccessors}
                    />
                ) : (
                    <p>No tasks to do</p>
                )}
                    

                </span>
                <a href="/create-task"><button className="create-task-btn">Create Task</button></a>
                
                <h2>My Completed Tasks</h2>
                {setUpDataCompleted(tasksToDo).length > 0 ? (
                    <TaskList
                    dataToUse={setUpDataCompleted(tasksToDo)}
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