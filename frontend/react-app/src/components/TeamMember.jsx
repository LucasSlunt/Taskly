import "../css/TeamTasks.css";

function TeamMember({ member,isAdminPage }){
    return(
    <div className="team-member">
      <span className="avatar">👤</span>
      <span className="name">{member.name} {member.role === "You" && "(You)"}</span>

      {member.role === "Admin" && <span className="role admin">Admin</span>}
      {isAdminPage && member.role !== "Admin" && (<button className="remove-btn">Remove</button>)}
    </div>
    );
}
export default TeamMember;