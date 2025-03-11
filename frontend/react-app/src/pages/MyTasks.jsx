import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/MyTasks.css"
import fakeData from "../FakeData/fakeTaskData.json"


function setUpDataComplete(obj){
 let ansArr = []
 console.log(fakeData)
 fakeData.map((taskItem) =>{
   if(taskItem.status === "done"){
    ansArr = [...ansArr,
        {
            name: taskItem.name,
            team: taskItem.team,
            assignees: taskItem.assignees,
            dueDate: taskItem.dueDate,
            dateCompteted: taskItem.dateCompteted


        }]
   }
 }
)
console.log(ansArr)
if(ansArr.length > 0){
    return ansArr
}else{
    return [" "]
}
}
function setUpDataTasksToDo(obj){
    let ansArr = []
    fakeData.map((taskItem) =>{
   if(taskItem.status !== "done"){
    ansArr = [ ...ansArr,
        {
            name: taskItem.name,
            team: taskItem.team,
            assignees: taskItem.assignees,
            status: taskItem.status,
            priority: taskItem.priority,
            dueDate: taskItem.dueDate
            


        }]
    
   }
 }
)
console.log(ansArr)
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
        },
        {
            Header: "Team",
            accessor:"team",
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
        },
        {
            Header: "Team",
            accessor:"team",
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