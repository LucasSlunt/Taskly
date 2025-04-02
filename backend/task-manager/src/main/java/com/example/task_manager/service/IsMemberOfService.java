package com.example.task_manager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.task_manager.DTO.IsMemberOfDTO;
import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

import jakarta.transaction.Transactional;

@Service // Marks this class as a Spring service, making it eligible for dependency injection
@Transactional
public class IsMemberOfService {
	
	private final IsMemberOfRepository isMemberOfRepository;
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final NotificationService notifService;

	// Constructor injection for required repositories
	public IsMemberOfService(IsMemberOfRepository isMemberOfRepository, 
							TeamRepository teamRepository, 
							TeamMemberRepository teamMemberRepository,
							NotificationService notifService) {
		this.isMemberOfRepository = isMemberOfRepository;
		this.teamRepository = teamRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.notifService = notifService;
	}

	/**
	 * Adds a TeamMember to a Team.
	 * Prevents duplicate assignments by checking if the member is already in the team.
	 *
	 * @param teamMemberId The ID of the team member to be added.
	 * @param teamId The ID of the team to which the member should be added.
	 * @return 
	 */
    public IsMemberOfDTO addMemberToTeam(int teamMemberId, int teamId) {
        System.out.println("BELLO it is running yay");

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

        System.out.println("MINIONS");

        // Check if the member is already in the team
        boolean alreadyMember = isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(teamMemberId, teamId);
        if (alreadyMember) {
            throw new RuntimeException("Team Member is already in this team. No action needed.");
        }

        System.out.println("TONIGHT!!! WE! ARE GOING! TO STEAL! THE MOON!!");

        // Create membership
        IsMemberOf isMemberOf = new IsMemberOf(teamMember, team);
        isMemberOf = isMemberOfRepository.save(isMemberOf);
        isMemberOfRepository.flush();

        System.out.println("DOCTOR NEFARIO");

        team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("RAHHHH can't find it"));
        System.out.println("bruh");

        //call assigned to team notification method
        notifService.notifyTeamAssignment(teamMember, team);

        // Return DTO
        return convertToDTO(isMemberOf);
    }
    
    //add multiple members to a team at once
    public List<IsMemberOfDTO> massAssignToTeam(int teamId, List<Integer> teamMemberIds) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

        List<IsMemberOfDTO> newMembers = new ArrayList<>();

        for (Integer teamMemberId : teamMemberIds) {
            TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                    .orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

            boolean alreadyMember = isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(teamMemberId, teamId);

            if (!alreadyMember) {
                IsMemberOf isMemberOf = new IsMemberOf(teamMember, team);
                isMemberOf = isMemberOfRepository.save(isMemberOf);

                notifService.notifyTeamAssignment(teamMember, team);

                newMembers.add(convertToDTO(isMemberOf));
            }
        }

        isMemberOfRepository.flush();
        
        return newMembers;
    }

	/**
	 * Removes a TeamMember from a Team.
	 * If the member is not part of the team, no changes are made.
	 *
	 * @param teamMemberId The ID of the team member to be removed.
	 * @param teamId The ID of the team from which the member should be removed.
	 * @return 
	 */
	public IsMemberOfDTO removeMemberFromTeam(int teamMemberId, int teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		// Find the membership record
		IsMemberOf isMemberOf = isMemberOfRepository.findByTeamMemberAndTeam(teamMember, team)
			.orElseThrow(() -> new RuntimeException("Membership not found."));

		isMemberOfRepository.delete(isMemberOf);

		//call unassigned from team notification method
		notifService.notifyTeamUnassignment(teamMember, team);

		// Return removed membership as DTO
		return convertToDTO(isMemberOf);
	}

	/**
	 * Checks if a TeamMember is a part of a specific Team.
	 *
	 * @param teamMemberId The ID of the team member.
	 * @param teamId The ID of the team.
	 * @return true if the team member is part of the team, false otherwise.
	 */
	public boolean isMemberOfTeam(int teamMemberId, int teamId) {
		return isMemberOfRepository.existsByTeamMemberAccountIdAndTeamTeamId(teamMemberId, teamId);
	}

	/**
	 * Converts IsMemberOf entity to IsMemberOfDTO.
	 */
	private IsMemberOfDTO convertToDTO(IsMemberOf isMemberOf) {
		return new IsMemberOfDTO(
			isMemberOf.getId(),
			isMemberOf.getTeam().getTeamId(),
			isMemberOf.getTeamMember().getAccountId()
		);
	}
}
