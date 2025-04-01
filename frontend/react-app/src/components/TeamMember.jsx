import "../css/TeamTasks.css";
import RemoveUserButton from "./RemoveUserButton";

function TeamMember({ member,isAdminPage, teamLeadId, setTeamMembers, teamId, teamMembers}){
    return(
    <div className="team-member">
      <span className="avatar">👤</span>
      <span className="name">{member.userName} {member.role === "You" && "(You)"}</span>
      {member.role === "ADMIN" && <span className="role" >Admin</span>}
      {member.accountId === teamLeadId && <span className="role" style={{backgroundColor:'#2b0fa7'}}>Team Lead</span>}
      {isAdminPage && member.accountId !== teamLeadId && (<RemoveUserButton userId={member.accountId} setTeamMembers = {setTeamMembers} teamMembers={teamMembers} teamId={teamId}/>)}
    </div>
    );
}
export default TeamMember;