import "../css/Teams.css"

function Teams({team}){
    return(
        <div className="teams">
            <button className="team-btn">
                <div className="team-info">
                    <h1>{team.id}</h1>
                    <h1>{team.name}</h1>
                </div>
            </button>
        </div>
    );
}

export default Teams