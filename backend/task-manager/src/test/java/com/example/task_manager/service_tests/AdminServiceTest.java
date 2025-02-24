package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.AdminDTO;
import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TeamDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.*;
import com.example.task_manager.service.AdminService;
import com.example.task_manager.service.TeamService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminServiceTest {

	@Autowired
	private AdminService adminService;

	@Autowired
	private TeamService teamService;

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

	private AdminDTO admin;
	private TeamMemberDTO teamMember;
	private TaskDTO unlockedTask;
	private TaskDTO lockedTask;
	private Team team;

	/*
	 * Added nanoTime to each string to ensure no entities with the same name are added twice, which will cause an error
	 */

	@BeforeEach
	void setUp() {
		isAssignedRepository.deleteAllInBatch();
		isMemberOfRepository.deleteAllInBatch();
		taskRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
		teamMemberRepository.deleteAllInBatch();
		adminRepository.deleteAllInBatch();
		authInfoRepository.deleteAllInBatch();

		admin = adminService.createAdmin("Admin Name", "admin" + System.nanoTime() + "@example.com");
		teamMember = adminService.createTeamMember("TM Name", "teamMember" + System.nanoTime() + "@example.com");

		team = new Team();
		team.setTeamName("Team Name " + System.nanoTime());
		team = teamRepository.save(team);

		unlockedTask = adminService.createTask("Unlocked Task", null, false, "Open", null, null, team, null);
		lockedTask = adminService.createTask("Locked Task", null, false, "Open", null, null, team, null);
	}

	// Try to create admin account
	@Test
	void testCreateAdmin() {
		AdminDTO adminDTO = adminService.createAdmin("Admin Name Testing", "admin" + System.nanoTime() + "@example.com");

		assertNotNull(adminDTO);
		assertEquals("Admin Name Testing", adminDTO.getUserName());
	}	

	// try to create admin with the same name
	@Test
	void testCreateAdminExistingName() {
		AdminDTO adminDTO = adminService.createAdmin("John Doe", "admin" + System.nanoTime() + "@example.com");

		Exception exception = assertThrows(RuntimeException.class,
				() -> adminService.createAdmin("John Doe", "admin" + System.nanoTime() + "@example.com"));

		assertNotNull(adminDTO);
	}
	
	// try to create admin with the same email
	@Test
	void testCreateAdminExistingEmail() {
		AdminDTO adminDTO = adminService.createAdmin("John Doe", "admin_email@example.com");

		Exception exception = assertThrows(RuntimeException.class,
				() -> adminService.createAdmin("Alice Wonder", "admin_email@example.com"));

		assertNotNull(adminDTO);
	}
	
	@Test
	void testDeleteAdmin() {
		adminService.deleteAdmin(admin.getAccountId());
		Optional<Admin> found = adminRepository.findById(admin.getAccountId());
		assertFalse(found.isPresent());
	}

	@Test
	void testModifyAdminName() {
		AdminDTO updatedAdminDTO = adminService.modifyAdminName(admin.getAccountId(), "New Name");
		assertEquals("New Name", updatedAdminDTO.getUserName());
	}

	@Test
	void testModifyAdminEmail() {
		AdminDTO updatedAdmin = adminService.modifyAdminEmail(admin.getAccountId(), "newEmail" + System.nanoTime() + "@example.com");
		assertTrue(updatedAdmin.getUserEmail().startsWith("newEmail"));
	}

	@Test
	void testModifyNonExistentAdmin() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> adminService.modifyAdminName(9999, "New Name"));

		assertNotNull(exception);
	}

	@Test
	void testCreateTeamMember() {
		TeamMemberDTO teamMemberDTO = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");

		assertNotNull(teamMemberDTO);
		assertEquals("Team Member", teamMemberDTO.getUserName());
	}

	@Test
	void testDeleteTeamMember() {
		adminService.deleteTeamMember(teamMember.getAccountId());
		Optional<TeamMember> found = teamMemberRepository.findById(teamMember.getAccountId());
		assertFalse(found.isPresent());
	}

	@Test
	void testModifyTeamMemberName() {
		TeamMemberDTO updatedTeamMember = adminService.modifyTeamMemberName(teamMember.getAccountId(), "New Name");
		assertEquals("New Name", updatedTeamMember.getUserName());
	}

	@Test
	void testModifyTeamMemberEmail() {
		TeamMemberDTO newTeamMember = adminService.createTeamMember("Team Member Email Test", "teamMemberEmailTest@example.com");

		String newEmail = "newEmail@example.com";
		TeamMemberDTO updatedTeamMember = adminService.modifyTeamMemberEmail(newTeamMember.getAccountId(), newEmail);

		assertEquals(newEmail, updatedTeamMember.getUserEmail());
	}

	@Test
	void testModifyTeamMemberNonexistent() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> adminService.modifyTeamMemberName(9999, "New Name"));

		assertNotNull(exception);
	}
	
    //to be implemented with hashedPassword
	// @Test
	// void testPromoteToAdmin() {
	// 	TeamMemberDTO teamMember = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");
	// 	AdminDTO updatedAdmin = adminService.promoteToAdmin(teamMember.getAccountId());

	// 	assertTrue(adminRepository.existsById(updatedAdmin.getAccountId()));
	// 	assertFalse(teamMemberRepository.findById(teamMember.getAccountId()).isPresent());
	// }

	@Test
	void testAssignToTeam() {
		TeamMemberDTO teamMember = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");
		TeamMemberDTO teamLead = adminService.createTeamMember("Team Lead", "lead" + System.nanoTime() + "@example.com");

		TeamDTO team = teamService.createTeam("Team Name " + System.nanoTime(), teamLead.getAccountId());

		adminService.assignToTeam(teamMember.getAccountId(), team.getTeamId());
		
		TeamMember updatedTeamMember = teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow();
		updatedTeamMember.getTeams().size();

		assertTrue(updatedTeamMember.getTeams().stream()
				.anyMatch(isMember -> isMember.getTeam().getTeamId() == team.getTeamId()));
	}

	@Test
	void testAssignToNonexistentTeam() {
		TeamMemberDTO teamMember = adminService.createTeamMember("Team Member", "test@example.com");

		Exception exception = assertThrows(RuntimeException.class, 
			() -> adminService.assignToTeam(teamMember.getAccountId(), 9999));

		assertNotNull(exception);
	}

	@Test
	void testLockTask() {
		adminService.lockTask(unlockedTask.getTaskId());
		Task updatedTask = taskRepository.findById(unlockedTask.getTaskId()).orElseThrow();
		assertTrue(updatedTask.isLocked());
	}

	@Test
	void testUnlockTask() {
		adminService.unlockTask(lockedTask.getTaskId());
		Task updatedTask = taskRepository.findById(lockedTask.getTaskId()).orElseThrow();
		assertFalse(updatedTask.isLocked());
	}

	@Test
	void testLockNonexistentTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> adminService.lockTask(9999));

		assertNotNull(exception);
	}
}
