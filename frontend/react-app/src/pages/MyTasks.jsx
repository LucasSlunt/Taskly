import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/MyTasks.css"



function MyTasks(){


    return (
        <div class="MyTasksPage">
            <Header/>
          <div class="content-wrapper">
                
                <h1>My Tasks</h1>
                <span class ="taskBox">
                    <TaskList/>
                </span>
                
                <h2>My Completed Tasks</h2>
                <span class ="taskBox">
                    <TaskList/>
                </span>
                
          </div>
        </div>
      );
      

}

export default MyTasks