import SearchFilterSort from "../components/SearchFilterSort";
import TaskList from "../components/TaskList";
import Header from "../components/Header";
import "../css/TeamTasks.css";
import TeamMember from "../components/TeamMember";


function TeamTasks(){

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
            <TaskList/>
            <button className="create-task-btn">Create Task</button>
            <h2>Completed Tasks</h2>
            <TaskList/>
            <h2>Team 1 Name</h2>
            <p>Description of what the team is working on. idk?</p>

            <h2>Team Members</h2>
            <div className="team-list">
              {members.map((member) => (
                <TeamMember key={member.id} member={member} />
              ))}
            </div>

            


          </div>
        </div>

          
      );
      

}

export default TeamTasks