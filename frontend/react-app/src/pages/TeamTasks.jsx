import { Link } from "react-router-dom";
import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/TeamTasks.css";
import TeamMember from "../components/TeamMember";
import fakeData from "../FakeData/fakeTaskData.json"

function getAssignnesNames(task){
  let returnArr = []
  task.assignees.map((assigne)=>{
      returnArr = [...returnArr, assigne.name]
  })
  return returnArr
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
      },
      {
        Header: "Is Locked",
        accessor: "isLocked",
      }
]
function setUpDataTasksToDo(obj){
  let ansArr = []
  fakeData.map((taskItem) =>{
  if(taskItem.status !== "done"){
    ansArr = [ ...ansArr,
        {
            id: taskItem.id,
            name: taskItem.name,
            assignees: getAssignnesNames(taskItem).join(' '),
            status: taskItem.status,
            priority: taskItem.priority,
            dueDate: taskItem.dueDate,
            isLocked: taskItem.isLocked
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
function setUpDataComplete(obj){
  let ansArr = []
  console.log(fakeData)
  fakeData.map((taskItem) =>{
    if(taskItem.status === "done"){
     ansArr = [...ansArr,
         {
            id: taskItem.id,
            name: taskItem.name,
            assignees: getAssignnesNames(taskItem).join(' '),
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
        <div className="team-tasks-page">
          <Header/>
          
          <div className="content-wrapper">
            <h2>Team 1 Tasks</h2>
            <TaskList
            dataToUse={setUpDataTasksToDo(fakeData)}
            headersAndAccessors={headerAndAccessors}
            />
            <button className="create-task-btn">Create Task</button>
            <h2>Completed Tasks</h2>
            <TaskList
            dataToUse={setUpDataComplete(fakeData)}
            headersAndAccessors={headerAndAccessorsComplete}
            />
            

            <h2>Team Members</h2>
            <div className="team-list">
              {members.map((member) => (
                <TeamMember key={member.id} member={member} isAdminPage={isAdmin}/>
              ))}
            </div>

            


          </div>
        </div>

          
      );
      

}

export default TeamTasks