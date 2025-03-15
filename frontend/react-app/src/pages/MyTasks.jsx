import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/MyTasks.css"
import fakeData from "../FakeData/fakeTaskData.json"
import { Link } from 'react-router-dom';


function setUpDataComplete(obj){
 let ansArr = []
 fakeData.map((taskItem) =>{
   if(taskItem.status === "done"){
    ansArr = [...ansArr,
        {
            id: taskItem.id,
            name: taskItem.name,
            team: taskItem.team,
            assignees: getAssignnesNames(taskItem),
            dueDate: taskItem.dueDate,
            dateCompteted: taskItem.dateCompteted


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
function getAssignnesNames(task){
    let returnArr = []
    task.assignees.map((assigne)=>{
        returnArr = [...returnArr, assigne.name]
    })
    return returnArr
}

function setUpDataTasksToDo(obj){
    let ansArr = []
    fakeData.map((taskItem) =>{
   if(taskItem.status !== "done"){
    console.log(taskItem.assignees)
    ansArr = [ ...ansArr,
        {
            id: taskItem.id,
            name: taskItem.name,
            team: taskItem.team,
            assignees: getAssignnesNames(taskItem).join(' '),
            status: taskItem.status,
            priority: taskItem.priority,
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
function MyTasks(){

    const headerAndAccessors = [
        {
            Header: "Task Name",
            accessor: "name",
            Cell: (original) => (
                <Link to="/view-task" state={{taskToSee: original.cell.row.values.id}}>{original.value}</Link>
              )
        },
        {
            Header: "Team",
            accessor:"team",
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
            Header: "Status",
            accessor: "status",
        },
        {
            Header: "Priority",
            accessor: "priority",
        },
        {
            Header: "Due Date",
            accessor: "dueDate",
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
            Header: "Team",
            accessor:"team",
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

    return (
        <div class = "MyTasksPage">
            <div class="pageFlexbox">
                <Header/>
            <div class="content-wrapper flexbox">
                    
                    <h1>My Tasks</h1>
                    <span class ="taskBox">
                        <TaskList
                        dataToUse={setUpDataTasksToDo(fakeData)}
                        headersAndAccessors={headerAndAccessors}
                        />
                    </span>
                    <a href="/create-task"><button className="create-task-btn">Create Task</button></a>
                    
                    <h2>My Completed Tasks</h2>
                    <span class ="taskBox">
                        <TaskList
                        dataToUse={setUpDataComplete(fakeData)}
                        headersAndAccessors={headerAndAccessorsComplete}
                        />
                    </span>
                    
            </div>
            </div>
        </div>
      );
      

}

export default MyTasks