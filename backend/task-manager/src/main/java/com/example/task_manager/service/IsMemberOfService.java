package com.example.task_manager.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

@Service // Marks this class as a Spring service, making it eligible for dependency injection
public class IsMemberOfService {
	
	private final IsMemberOfRepository isMemberOfRepository;
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;

	// Constructor injection for required repositories
	public IsMemberOfService(IsMemberOfRepository isMemberOfRepository, 
							 TeamRepository teamRepository, 
							 TeamMemberRepository teamMemberRepository) {
		this.isMemberOfRepository = isMemberOfRepository;
		this.teamRepository = teamRepository;
		this.teamMemberRepository = teamMemberRepository;
	}

	/**
	 * Adds a TeamMember to a Team.
	 * Prevents duplicate assignments by checking if the member is already in the team.
	 *
	 * @param teamMemberId The ID of the team member to be added.
	 * @param teamId The ID of the team to which the member should be added.
	 */
	public void addMemberToTeam(int teamMemberId, int teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));
	
		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));
	
		// Check if the member is already in the team
		if (team.getMembers().stream()
			.anyMatch(isMemberOf -> isMemberOf.getTeamMember().getAccountId() == teamMemberId)) {
			throw new RuntimeException("Team Member is already in this team. No action needed.");
		}

		// Create and save the membership record
		IsMemberOf isMemberOf = new IsMemberOf();
		isMemberOf.setTeam(team);
		isMemberOf.setTeamMember(teamMember);
		isMemberOfRepository.save(isMemberOf);
		
		// Maintain bidirectional relationship
		team.getMembers().add(isMemberOf);
		teamRepository.save(team);

		// Ensure changes are persisted immediately
		teamRepository.flush();
	}

	/**
	 * Removes a TeamMember from a Team.
	 * If the member is not part of the team, no changes are made.
	 *
	 * @param teamMemberId The ID of the team member to be removed.
	 * @param teamId The ID of the team from which the member should be removed.
	 */
	public void removeMemberFromTeam(int teamMemberId, int teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

		TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
			.orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + teamMemberId));

		// Find the membership record
		Optional<IsMemberOf> isMemberOf = team.getMembers().stream()
			.filter(member -> member.getTeamMember().getAccountId() == teamMemberId)
			.findFirst();

		if (isMemberOf.isPresent()) {
			// Remove from team and delete the membership record
			team.getMembers().remove(isMemberOf.get());
			isMemberOfRepository.delete(isMemberOf.get());
		} else {
			System.out.println("Team Member is not in this team. No action needed.");
		}
	}

	/**
	 * Checks if a TeamMember is a part of a specific Team.
	 *
	 * @param teamMemberId The ID of the team member.
	 * @param teamId The ID of the team.
	 * @return true if the team member is part of the team, false otherwise.
	 */
	public boolean isMemberOfTeam(int teamMemberId, int teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

		return team.getMembers().stream()
			.anyMatch(isMemberOf -> isMemberOf.getTeamMember().getAccountId() == teamMemberId);
	}
}
