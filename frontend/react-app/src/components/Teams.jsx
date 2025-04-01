import "../css/Teams.css"
import {Link} from 'react-router-dom'

//team buttons (don't have onclick functionality yet)
function Teams({team}){
    return(
        <div className="teams">
            <Link className="teamButton headerText1" to='/team-tasks' state={{ teamId: team.teamId}} key={team.teamId} >
                            {team.teamName}
            </Link>
        
        </div>
    );
}

export default Teams