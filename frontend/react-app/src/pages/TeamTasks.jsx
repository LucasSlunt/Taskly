import SearchFilterSort from "../components/SearchFilterSort";
import TaskList from "../components/TaskList";
import "../css/TeamTasks.css";


function TeamTasks(){


    return (
        <div className="team-tasks-page">
          <div className="content-wrapper">
            <h2>Team 1 Tasks</h2>
            <TaskList/>
          </div>
        </div>
      );
      

}

export default TeamTasks