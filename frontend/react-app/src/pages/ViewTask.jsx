import "../css/ViewTask.css"
import Header from "../components/Header.jsx"
import {useLocation} from 'react-router-dom'
import fakeTaskData from '../FakeData/fakeTaskData.json'
function getAssignnesNames(task){
    let returnArr = []
    task.assignees.map((assigne)=>{
        returnArr = [...returnArr, (assigne.name+" ")]
    })
    return returnArr
}
function ViewTask(){
    const location = useLocation()
    const { taskToSee } = location.state
    console.log(taskToSee)
    function getTask (){
        //We will do an API GET call for the task here
        let returnArr =[]
        fakeTaskData.map((task)=>{
            if(task.id === taskToSee){
                returnArr = [...returnArr, {
                    id: task.id,
                    name: task.name,
                    team: task.team,
                    assignees: getAssignnesNames(task),
                    dueDate: task.dueDate,
                    priority: task.priority,
                    discription: task.discription,
                    dateCompteted: task.dateCompteted
                }]
            }
        })
        if(returnArr.length ===0){
            return {name: "TaskFailedToLoad"}
        }
        return returnArr[0]
    }
    
    return(
        <div class = "viewTask">
            <Header/>
            <div class="page-body">
                <div class="flexbox">
                    <div class="header"style={{ whiteSpace: 'pre-line' }}>
                    <strong class ="nameOfTask">{getTask().name + "\n"}</strong> Assigned to: {getTask().assignees}
                    </div>
                    {/* Place holder for when we get our database we will get the prority DB*/}
                    <div class="priority">
                        Priority: {getTask().priority}
                    </div>
                </div>
                <div class="discription">
                    {getTask().discription}
                </div>
                {/*<div class="pic">
                    <ul>
                        <li class="imgSpacing"><img src={getTask.pic1} alt="" /></li>
                        <li class="imgSpacing"><img src={getTask.pic2} alt="" /></li>
                    </ul>

                </div>*/}
                <div class="comment-section">
                    <form action="">
                        <input type="text" name="Comment" id="comment" placeholder = "Add Comment/Note"class = "comment"/>
                        <input type="button" value="SUBMIT" class ="submit-comment"/>
                    </form>
                </div>
                <div class="update-class">
                    <div class = "updateStatus">
                        <select name="update status" id="newStatus" class = "updateSelector">
                            <option value="" disabled selected>Update Status</option>
                            <option value="notStarted">Not Started</option>
                            <option value="InProgress">In Progress</option>
                            <option value="Done">Done</option>
                        </select>
                        <Link to = {'/edit-task'} state={{taskToEdit: task, onThisTeam: team}}>
                            <button class="fotterbutton">EDIT</button>
                            </Link>
                    </div>
                    <input type="button" value="DELETE TASK" class="fotterbutton"/>

                </div>
            </div>
        </div>
    )
}
export default ViewTask
//as = {Link} to = {'/edit-task'} state={{taskToEdit: task, onThisTeam: team}}