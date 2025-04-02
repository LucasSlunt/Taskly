import { deleteTeam } from "../api/teamApi"

export default function DeleteTeamButton({teamId}){
    async function deleteThisTeam(){
        try {
            const response = deleteTeam(teamId)
            await alert('Team Deleted')
            window.location.href="/home";
        } catch (error) {
            
        }
    }
    return(
        <button onClick={deleteThisTeam} className="importButton">Delete Team</button>
    )
}