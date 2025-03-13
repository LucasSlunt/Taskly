package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.service.IsMemberOfService;
import com.example.task_manager.service.TeamService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class TeamServiceTest {

	@Autowired
	private TeamService teamService;

	@Autowired
	private IsMemberOfService isMemberOfService;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private TeamMemberRepository teamMemberRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private IsAssignedRepository isAssignedRepository;

	@Autowired
	private IsMemberOfRepository isMemberOfRepository;
	
	@Autowired
	private AuthInfoRepository authInfoRepository;

	private TeamDTO team;
	private TeamMember teamLead;
	private TeamMember newTeamLead;
	private TeamMember teamMember;

	@BeforeEach
	void setUp() {
		isAssignedRepository.deleteAll();
		isMemberOfRepository.deleteAll();
		taskRepository.deleteAll();
		teamMemberRepository.deleteAll();
		authInfoRepository.deleteAll();
		adminRepository.deleteAll();
		teamRepository.deleteAll();

		teamLead = new TeamMember("Team Lead", "team_lead" + System.nanoTime() + "@example.com","defaultpw");
		teamLead = teamMemberRepository.save(teamLead);

		newTeamLead = new TeamMember("New Team Lead", "new_team_lead" + System.nanoTime() + "@example.com","defaultpw");
		newTeamLead = teamMemberRepository.save(newTeamLead);

		teamMember = new TeamMember("Team Member", "team_member" + System.nanoTime() + "@example.com","defaultpw");
		teamMember = teamMemberRepository.save(teamMember);

		team = teamService.createTeam("Development Team " + System.nanoTime(), teamLead.getAccountId());
	}

	@Test
	void testCreateTeam() {
		String teamName = "QA Team " + System.nanoTime();
		TeamDTO newTeam = teamService.createTeam(teamName, teamLead.getAccountId());

		assertNotNull(newTeam);
		assertEquals(teamName, newTeam.getTeamName());
		assertEquals(teamLead.getAccountId(), newTeam.getTeamLeadId());
	}

	@Test
	void testCreateTeamWithEmptyName() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamService.createTeam("", teamLead.getAccountId()));

		assertTrue(exception.getMessage().contains("Team name cannot be empty"));
	}

	@Test
	void testCreateTeamWithNonExistentLead() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamService.createTeam("New Team", 9999));

		assertTrue(exception.getMessage().contains("Team Lead not found"));
	}

	@Test
	void testDeleteTeam() {
		teamService.deleteTeam(team.getTeamId());

		Optional<Team> deletedTeam = teamRepository.findById(team.getTeamId());
		assertFalse(deletedTeam.isPresent());
	}

	@Test
	void testDeleteNonExistentTeam() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamService.deleteTeam(9999));

		assertTrue(exception.getMessage().contains("Team not found"));
	}

	@Test
    void testChangeTeamLead() {
        int teamId = team.getTeamId();
        String newTeamName = "Updated Team Name";
        int newTeamLeadId = newTeamLead.getAccountId();

        TeamDTO updatedTeamDTO = teamService.changeTeamLead(teamId, newTeamName, newTeamLeadId);

        assertNotNull(updatedTeamDTO);
        assertEquals(newTeamName, updatedTeamDTO.getTeamName());
        assertEquals(newTeamLeadId, updatedTeamDTO.getTeamLeadId());

        Team updatedTeam = teamRepository.findById(teamId).orElseThrow();
        assertEquals(newTeamName, updatedTeam.getTeamName());
        assertEquals(newTeamLeadId, updatedTeam.getTeamLead().getAccountId());
    }

	@Test
    void testChangeTeamLeadToNonExistentMember() {
        int teamId = team.getTeamId();
        String newTeamName = "Updated Team Name";

        Exception exception = assertThrows(RuntimeException.class, 
            () -> teamService.changeTeamLead(teamId, newTeamName, 9999));

        assertTrue(exception.getMessage().contains("Team Lead not found"));
    }

	@Test
	void testGetTeamMembers() {
		isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

		List<TeamMemberDTO> teamMembers = teamService.getTeamMembers(team.getTeamId());

		assertNotNull(teamMembers);
		assertFalse(teamMembers.isEmpty());

		assertTrue(teamMembers.stream()
			.anyMatch(t -> t.getAccountId() == teamMember.getAccountId()));
	}

	@Test
	void testGetMembersOfNonExistentTeam() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamService.getTeamMembers(9999));

		assertTrue(exception.getMessage().contains("Team not found"));
	}

	@Test
	void testAddMemberToNonExistentTeam() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> isMemberOfService.addMemberToTeam(teamMember.getAccountId(), 9999));

		assertTrue(exception.getMessage().contains("Team not found"));
	}

	@Test
	void testGetMembersOfTeamWithNoMembers() {
		List<TeamMemberDTO> members = teamService.getTeamMembers(team.getTeamId());
		assertTrue(members.isEmpty());
	}
}
