import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/MyTasks.css"



function MyTasks(){


    return (
        <div class = "MyTasksPage">
            <div class="pageFlexbox">
                <Header/>
            <div class="content-wrapper flexbox">
                    
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
        </div>
      );
      

}

export default MyTasks