import {useTable, useSortBy} from 'react-table'
import React, { useEffect } from 'react';
import "../css/TaskList.css";
import "../css/UserTable.css";
import SearchFilterSort from './SearchFilterSort';
import { useState} from 'react';
import { getTeamMembers } from '../api/teamApi';
import {Link} from 'react-router-dom'
import { changeRole } from '../api/adminApi';
import { deleteAdmin } from '../api/adminAccountApi';
import { deleteTeamMember } from '../api/teamMemberAccountApi';

const AllTeams = [
    {
        name: "Team1",
        id: "1",
        members: [
            {
                name:"Liam",
                memberID: "0",
                email: "liamDig@jmail.com",
                role: "admin"
            }, 
            {
                name:"Greg",
                memberID: "1",
                email: "GregDig@jmail.com",
                role: "teamMember"
        
            },
            {
                name: "Mel",
                memberID: "4",
                email: "MelDig@jmail.com",
                role: "admin"
    
            }]
    },
    {
        name: "Team2",
        id: "2",
        members: [
        {
            name:"Liam",
            memberID: "0",
            email: "liamDig@jmail.com",
            role: "admin"
        }, 
        {
            name:"Greg",
            memberID: "1",
            email: "GregDig@jmail.com",
            role: "teamMember"
    
        }, 
        {
            name:"Gorgia",
            memberID: "2",
            email: "GorgiaDig@jmail.com",
            role: "teamLead"
        }]
    }
    ]
function UserTable({teams}){

    const [loading, setLoading] = useState(true);

    const deleteUser = async (event, role)=>{
        console.log("delete THIS USER ")
        console.log(event.target.value)
        try {
            if(role === 'ADMIN'){
                const response = await deleteAdmin(event.target.value)
                alert('ADMIN DELETED')
            }else{
                const response = await deleteTeamMember(event.target.value)
                alert('TEAM MEMBER DELETED')
            }
        } catch (error) {
            
        }
        window.location.reload();

    }
    
    const changeUserRole = async (userID, event)=>{
        console.log("rolesChanged")
        console.log(userID, event.target.value)
        try {
            const response = await changeRole(userID, event.target.value)
            if(response){
                alert("ROLE CHANGED ID WILL CHANGE ALSO CHECK LIST FOR ID")
            }else{
                throw Error("FAILED TO UPDATE ROLE")
            }
        } catch (error) {
            alert(error)
        }
        window.location.reload();
    }
    

    const [loadThisTeam, setTeam] = useState(()=>{
        let firstSetOfData = []
        AllTeams[0].members.map((member)=>{
            firstSetOfData.push({name: member.name, role: member.role, del: member.memberID})
        })
        return firstSetOfData
    })
    
    const changeSearch = async(event) =>{
        setLoading(true);
        console.log("seeing a new team")
        try {
            let arrReturn = []
        const teamMembers = await getTeamMembers(event.target.value)
        console.log(teamMembers)
        if(teamMembers.length <1){
            arrReturn = [...arrReturn,{name: teamMembers.userName, role: teamMembers.role, id: teamMembers.accountId, del: teamMembers.accountId}]
        }else{
            teamMembers.map((member)=>{
                arrReturn = [...arrReturn,{name: member.userName, role: member.role, id: teamMembers.accountId, del: member.accountId}]
             })
        }
        console.log(arrReturn)
        setTeam(()=>(arrReturn))
        } catch (error) {
            alert(error)
        }finally{
            setLoading(false)
        }
      }

    

    const [searchQuery, setSearchQuery] = useState(""); //creates a state variable "searchQuery" and function "setSearchQuery" to update it
    //whenever the searchQuery changes, runs the useMemo and filters through the fakeData data to only the task names that contain the searchQuery 
    const data = React.useMemo(()=> {return loadThisTeam.filter((task) =>{
            return task.name.toLowerCase().includes(searchQuery.toLowerCase())}
    );}, [searchQuery, loadThisTeam]);
    const columns = React.useMemo(() => [
        {
            Header: "Name",
            accessor: "name",
        },
        {
            Header: "Role",
            accessor:"role",
            Cell: (original) => (
                <select className="cellSelect" id={"role "+original.cell.row.values.del} defaultValue={original.value} onChange={(e)=>changeUserRole(original.cell.row.values.del, e)}>
                    <option value="ADMIN">Admin</option>
                    <option value="TEAM_MEMBER">Team Member</option>
                </select>
              )
        },
        {
            Header: 'ID',
            accessor: 'id',
            Cell:(original) =>(
                <div>{original.cell.row.values.del}</div>
            )
        },
        {
            Header: "",
            accessor: "del",
            Cell: (original) => (
                <button class="userDeleteBtn" id= {"delete " + original.value} value={original.value} onClick={(e)=>deleteUser(e, original.cell.row.values.role)}>
                    Delete
                </button>
              )
        },
        {
            Header: "",
            accessor: "edit",
            Cell: (original) => (
                <Link to='/edit-user-details' state={{accountToEdit: original.cell.row.values.del}}>
                <button class="userEditBtn" key= {"edit" + original.cell.row.values.del} id ={"edit" + original.cell.row.values.del}>
                    Edit
                </button>
                </Link>
              )
        }
    ],[searchQuery, loadThisTeam]
    )
    useEffect(()=>{
        async function getFirstTeam() {
            try {
                const teamMembers = await getTeamMembers(teams[0].teamId)
                let arrReturn = [];
                console.log(teamMembers)
                if(teamMembers.length <1){
                    arrReturn = [...arrReturn,{name: teamMembers.userName, role: teamMembers.role, id: teamMembers.accountId, del: teamMembers.accountId}]
                }else{
                    teamMembers.map((member)=>{
                        arrReturn = [...arrReturn,{name: member.userName, role: member.role, id: teamMembers.accountId, del: member.accountId}]
                     })
                }
                console.log(arrReturn)
                setTeam(()=>(arrReturn))

            } catch (error) {
                alert(error)
            }finally{
                setLoading(false)
            }
        }
        getFirstTeam();
    },[])

    const { getTableBodyProps, getTableProps, rows, prepareRow, headerGroups} = useTable({columns,data: data}, useSortBy)
    return(
        
        <div className='container'>
            <select name="searchThis" id="searchThis" onChange={changeSearch}>
                    {teams.map((team)=>(
                        <option value={team.teamId}>{team.teamName}</option>
                    ))}
             </select>
            
            {/* pass searchQuery and setSearchQuery to SearchFilterSort component */}
            <SearchFilterSort
                searchQuery={searchQuery}
                setSearchQuery={setSearchQuery}
            />
            
            {loading && (<div>...loading</div>)}
            {!loading && (<table {...getTableProps()}>
                <thead>
                    {headerGroups.map((headerGroup) => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map((column) =>(
                                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                            {column.render("Header")}
                            <span>
                                {column.isSorted ? (column.isSortedDesc ? '⬇️':'⬆️'): '↕️'}
	                        </span>
                            </th>
                            ))}
                        </tr>
                    ))}
                </thead>
                <tbody{...getTableBodyProps()}>
                        {rows.map((row)=>{
                            prepareRow(row)
                            return( 
                                <tr key={row.id} {...row.getRowProps()}>
                                    {row.cells.map((cell)=>(
                                        <td key = {cell.id} {...cell.getCellProps()}>{cell.render("Cell")}</td>
                                    ))}
                                </tr>
                            )
                        })}
                </tbody>
            </table>)}
        </div>

    );
}

export default UserTable