package com.example.task_manager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

@Service // Marks this class as a Spring service, allowing it to be injected where needed
public class TeamService {
	
	private final TeamMemberRepository teamMemberRepository;
	private final TeamRepository teamRepository;
	private final IsMemberOfRepository isMemberOfRepository;

	// Constructor injection for required repositories
	public TeamService(TeamMemberRepository teamMemberRepository, 
					   TeamRepository teamRepository,
					   IsMemberOfRepository isMemberOfRepository) {
		this.teamMemberRepository = teamMemberRepository;
		this.teamRepository = teamRepository;
		this.isMemberOfRepository = isMemberOfRepository;
	}

	/**
	 * Creates a new team with the specified team name and team lead.
	 *
	 * @param teamName The name of the team.
	 * @param teamLeadId The ID of the team lead.
	 * @return The created Team entity.
	 */
	public Team createTeam(String teamName, int teamLeadId) {
		TeamMember teamLead = teamMemberRepository.findById(teamLeadId)
				.orElseThrow(() -> new RuntimeException("Team Lead not found with ID: " + teamLeadId));

		Team team = new Team();
		team.setTeamName(teamName);
		team.setTeamLead(teamLead);

		return teamRepository.save(team);
	}

	/**
	 * Deletes a team by its ID.
	 *
	 * @param teamId The ID of the team to delete.
	 */
	public void deleteTeam(int teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

		teamRepository.delete(team);
	}

	/**
	 * Changes the team lead of an existing team.
	 *
	 * @param teamId The ID of the team whose lead should be changed.
	 * @param teamLeadId The ID of the new team lead.
	 */
	public void changeTeamLead(int teamId, int teamLeadId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));

		TeamMember teamMember = teamMemberRepository.findById(teamLeadId)
				.orElseThrow(() -> new RuntimeException("Team Lead not found with ID: " + teamLeadId));

		team.setTeamLead(teamMember);
		teamRepository.save(team);
	}

	/**
	 * Retrieves all members of a specified team.
	 *
	 * @param teamId The ID of the team.
	 * @return A list of TeamMembers belonging to the team.
	 */
	public List<TeamMember> getTeamMembers(int teamId) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new RuntimeException("Team not found with ID: " + teamId));
	
		return isMemberOfRepository.findMembersByTeamId(teamId).stream()
			.map(IsMemberOf::getTeamMember)
			.collect(Collectors.toList());
	}
}

