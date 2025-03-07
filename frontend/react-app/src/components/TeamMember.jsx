import "../css/TeamTasks.css";

function TeamMember({ member }){
    return(
    <div className="team-member">
      <span className="avatar">👤</span>
      <span className="name">{member.name} {member.role === "You" && "(You)"}</span>
      {member.role === "Admin" && <span className="role admin">Admin</span>}
    </div>
    );
}
export default TeamMember;