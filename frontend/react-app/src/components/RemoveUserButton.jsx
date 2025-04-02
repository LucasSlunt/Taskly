import { removeMemberFromTeam } from "../api/isMemberOfApi"

export default function RemoveUserButton({userId, teamId,setTeamMembers, teamMembers}){
    async function removeUser(){
        try {
            const response = removeMemberFromTeam(userId,teamId)
            setTeamMembers(
                teamMembers.filter((teamMember)=>(teamMember.accountId !== userId))
            )
        } catch (error) {
            console.log(error)
        }
    }
    return(
        <button className='smallImportButton' onClick={removeUser}>Remove User</button>
    )}