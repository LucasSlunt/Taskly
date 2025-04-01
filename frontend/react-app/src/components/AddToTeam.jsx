import Select from 'react-select'
import {useForm, Controller} from 'react-hook-form'
import { addMemberToTeam } from '../api/isMemberOfApi';
export default function AddToTeam({teamId, allTeamMembers, currentMembers, setTeamMembers}){
    console.log('allMembers', allTeamMembers)
    const { reset, handleSubmit, formState: {errors}, control} = useForm();
    function formatTeamData(){
        let returnArr = []
        allTeamMembers.map((member)=>{
            let isOnTeam = false;
            currentMembers.map((currentMember)=>{
                if(member.accountId === currentMember){
                    isOnTeam = true;
                }
            })
            if(!isOnTeam){
                returnArr= [...returnArr, {value: member.accountId, label: member.userName, role: member.role}]
            }
        })
        return returnArr;
    }
    const onSubmit = async (data)=>{
        try {
            await data.assignees.map(async(assignee)=>{
                const response = await addMemberToTeam(assignee.value, teamId);
                setTeamMembers((prev)=>([...prev, {accountId: assignee.value, userName: assignee.label, role: assignee.role}]))
            })
            reset({})
        } catch (error) {
            console.log(error)
        }
    }
    const customStyles = {
        control: (provided) => ({
          ...provided,
          width: '100%',
        minWidth: '200px',
        maxWidth: '100%',
        minHeight: '30px',
        maxHeight: '100%',
          border: '2px solid grey',
          borderRadius: '10px',
          paddingLeft: '8px',
          backgroundColor: '#BFCDE0',
          margin: '10px 0px',
          cursor: 'pointer'
        }),
        option: (provided, state) =>({
            ...provided,
            cursor: state.isSelected ? 'default' : 'pointer',
        })
      };

      return(
        <form onSubmit={handleSubmit(onSubmit)} className ='rowFlexbox 'style={{justifyContent: 'stretch', width: '50%'}}>
            <Controller
                control={control}
                className='Select'
                name="assignees"
                rules={{required:true}}
                defaultValue={[]}
                render={({field}) => (
                    <Select
                        {...field}
                        options={formatTeamData()}
                        isMulti                  
                        styles={customStyles}               
                    />)}
            />
            <button type="submit" className='smallImportButton'>Add To Team</button>
        </form>
      )

}