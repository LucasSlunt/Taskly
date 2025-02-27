import "../css/TeamTasks.css";
import { useState } from "react";

function TeamTasks(){

    //for searching tasks
    const [searchQuery, setSearchQuery] = useState("");
    const handleSearch = (e) => {
        e.preventDefault() //stop from deleting user input after submit is clicked
    };


    return (
        <div className="team-tasks-page">
          <div className="content-wrapper">
            <h2>Team 1 Tasks</h2>
            <form onSubmit={handleSearch} className="search-form">
              <input
                type="text"
                placeholder="Search for task.."
                className="search-input"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
              <button type="submit" className="search-button">Search</button> 
            </form>
          </div>
        </div>
      );
      

}

export default TeamTasks