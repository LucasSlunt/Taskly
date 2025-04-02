import { changeTeamLead } from "../api/teamApi"

export default function ChangeTeamLeadButton({teamId, userId, setTeamLead}){
    async function onClick() {
        try {
            const response = await changeTeamLead(teamId, userId)
            console.log(response)
            setTeamLead(userId)
        } catch (error) {
            console.log(error)
        }
    }
    return(
        <button onClick={onClick} className="smallImportButton">Promote To Team Lead</button>
    )
}