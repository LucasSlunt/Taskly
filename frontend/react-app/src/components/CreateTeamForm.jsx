import { useForm, Controller} from "react-hook-form"; 
import Select from 'react-select'
import { createTeam } from "../api/teamApi";
import { assignTeamMemberToTeam } from "../api/adminApi";
export default function CreateTeamForm({users}){
    const {handleSubmit, register, control} = useForm();
    async function onSumbit(data){
        try {
            const teamLead = data.teamMembers.shift();
            const responseToCreateTeam = await createTeam(teamLead.value, data.teamName)
            if(data.teamMembers.length >0){
                data.teamMembers.map( async (teamMember)=>{
                    const responseToAddToTeam = assignTeamMemberToTeam(teamMember.value, responseToCreateTeam.teamId)
                })
            }
            await alert("Team Created with id"+ responseToCreateTeam.teamId)
            window.location.href="/home";
        } catch (error) {
            console.log(error)
        }
    }
    const customStyles = {
        control: (provided) => ({
          ...provided,
          width: '65vw',
        minWidth: '100px',
        maxWidth: '100%',
        minHeight: '30px',
        maxHeight: '100%',
          border: '2px solid grey',
          borderRadius: '10px',
          paddingLeft: '8px',
          backgroundColor: '#BFCDE0',
          margin: '10px 0px',
        }),
      };
    return(
        <form id="createAccountForm" onSubmit={handleSubmit(onSumbit)}>
            <label>Team Name</label>
            <input type="text" {...register("teamName", {required:true})}/>
            <label>Team Members (One Required, First Selected Will Be Team Lead)</label>
            <Controller
                    control={control}
                    defaultValue={[]}
                    className='Select'
                    id = 'selectTeamMembers'
                    name="teamMembers"
                    rules={{required:true}}
                     render={({field}) => (
                        <Select
                        {...field}
                        options={users}
                        isMulti
                        styles={customStyles}
                        
                        />)}
                    />
            <button type="submit">Create Team</button>
        </form>
    )
}