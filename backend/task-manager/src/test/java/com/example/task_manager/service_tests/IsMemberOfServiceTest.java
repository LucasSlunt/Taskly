package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.service.AdminService;
import com.example.task_manager.service.IsMemberOfService;
import com.example.task_manager.service.TeamService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class IsMemberOfServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private AdminService adminService;

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
    private TeamMemberDTO teamLead;
    private TeamMemberDTO teamMember;

    @BeforeEach
    void setUp() {
        // Clear DB to avoid conflicts
        isAssignedRepository.deleteAll();
		isMemberOfRepository.deleteAll();
		taskRepository.deleteAll();
		teamMemberRepository.deleteAll();
		authInfoRepository.deleteAll();
		adminRepository.deleteAll();
		teamRepository.deleteAll();

        teamLead = adminService.createTeamMember("Team Lead", "team_lead" + System.nanoTime() + "@example.com","defaultpw");
        team = teamService.createTeam("Development Team " + System.nanoTime(), teamLead.getAccountId());
        teamMember = adminService.createTeamMember("Team Member", "team_member" + System.nanoTime() + "@example.com","defaultpw");
    }


    @Test
    void testAddMemberToTeam() {
        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        assertTrue(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));
    }

    @Test
    void testAddMemberToNonExistentTeam() {
        Exception exception = assertThrows(RuntimeException.class, 
            () -> isMemberOfService.addMemberToTeam(teamMember.getAccountId(), 9999));

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testAddNonExistentMemberToTeam() {
        Exception exception = assertThrows(RuntimeException.class, 
            () -> isMemberOfService.addMemberToTeam(9999, team.getTeamId()));

        assertTrue(exception.getMessage().contains("Team Member not found"));
    }

    @Test
    void testAddMemberToSameTeamTwice() {
        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        Exception exception = assertThrows(RuntimeException.class, 
            () -> isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId()));

        assertTrue(exception.getMessage().contains("Team Member is already in this team"));
    }

    @Test
    void testRemoveMemberFromTeam() {
        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());
        isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId());

        assertFalse(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));
    }

    @Test
    void testRemoveNonExistentMemberFromTeam() {
        Exception exception = assertThrows(RuntimeException.class, 
            () -> isMemberOfService.removeMemberFromTeam(9999, team.getTeamId()));

        assertTrue(exception.getMessage().contains("Team Member not found"));
    }

    @Test
    void testRemoveMemberFromNonExistentTeam() {
        Exception exception = assertThrows(RuntimeException.class, 
            () -> isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), 9999));

        assertTrue(exception.getMessage().contains("Team not found"));
    }

    @Test
    void testRemoveMemberNotInTeam() {
        Exception exception = assertThrows(RuntimeException.class, 
            () -> isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId()));

        assertTrue(exception.getMessage().contains("Membership not found"));
    }

    @Test
    void testIsMemberOfTeam() {
        isMemberOfService.addMemberToTeam(teamMember.getAccountId(), team.getTeamId());

        assertTrue(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));

        isMemberOfService.removeMemberFromTeam(teamMember.getAccountId(), team.getTeamId());

        assertFalse(isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), team.getTeamId()));
    }

    @Test
    void testIsMemberOfNonExistentTeam() {
        boolean isMember = isMemberOfService.isMemberOfTeam(teamMember.getAccountId(), 9999);
        assertFalse(isMember);
    }

    @Test
    void testIsMemberOfNonExistentMember() {
        boolean isMember = isMemberOfService.isMemberOfTeam(9999, team.getTeamId());
        assertFalse(isMember);
    }
}
