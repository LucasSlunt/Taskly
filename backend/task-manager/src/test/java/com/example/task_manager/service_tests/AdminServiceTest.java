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

import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AdminRepository;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
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

	private Admin admin;
	private TeamMember teamMember;
	private Task unlockedTask;
	private Task lockedTask;
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
		Admin admin = adminService.createAdmin("Admin Name Testing", "admin" + System.nanoTime() + "@example.com");

		assertNotNull(admin);
		assertEquals("Admin Name Testing", admin.getUserName());
	}

	// try to create admin with the same name
	@Test
	void testCreateAdminExistingName() {
		Admin admin = adminService.createAdmin("John Doe", "admin" + System.nanoTime() + "@example.com");

		Exception exception = assertThrows(RuntimeException.class,
				() -> adminService.createAdmin("John Doe", "admin" + System.nanoTime() + "@example.com"));

		assertNotNull(admin);
	}
	
	// try to create admin with the same email
	@Test
	void testCreateAdminExistingEmail() {
		Admin admin = adminService.createAdmin("John Doe", "admin_email@example.com");

		Exception exception = assertThrows(RuntimeException.class,
				() -> adminService.createAdmin("Alice Wonder", "admin_email@example.com"));

		assertNotNull(admin);
	}
	
	@Test
	void testDeleteAdmin() {
		adminService.deleteAdmin(admin.getAccountId());
		Optional<Admin> found = adminRepository.findById(admin.getAccountId());
		assertFalse(found.isPresent());
	}

	@Test
	void testModifyAdminName() {
		Admin updatedAdmin = adminService.modifyAdminName(admin.getAccountId(), "New Name");
		assertEquals("New Name", updatedAdmin.getUserName());
	}

	@Test
	void testModifyAdminEmail() {
		Admin updatedAdmin = adminService.modifyAdminEmail(admin.getAccountId(), "newEmail" + System.nanoTime() + "@example.com");
		assertTrue(updatedAdmin.getUserEmail().startsWith("newEmail"));
	}

	@Test
	void testCreateTeamMember() {
		TeamMember teamMember = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");

		assertNotNull(teamMember);
		assertEquals("Team Member", teamMember.getUserName());
	}

	@Test
	void testDeleteTeamMember() {
		adminService.deleteTeamMember(teamMember.getAccountId());
		Optional<TeamMember> found = teamMemberRepository.findById(teamMember.getAccountId());
		assertFalse(found.isPresent());
	}

	@Test
	void testModifyTeamMemberName() {
		TeamMember updatedTeamMember = adminService.modifyTeamMemberName(teamMember.getAccountId(), "New Name");
		assertEquals("New Name", updatedTeamMember.getUserName());
	}

	@Test
	void testModifyTeamMemberEmail() {
		TeamMember newTeamMember = adminService.createTeamMember("Team Member Email Test", "teamMemberEmailTest@example.com");

		String newEmail = "newEmail@example.com";
		TeamMember updatedTeamMember = adminService.modifyTeamMemberEmail(newTeamMember.getAccountId(), newEmail);

		assertEquals(newEmail, updatedTeamMember.getUserEmail());
	}
	
	@Test
	void testPromoteToAdmin() {
		TeamMember teamMember = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");
		Admin updatedAdmin = adminService.promoteToAdmin(teamMember.getAccountId());

		assertTrue(adminRepository.existsById(updatedAdmin.getAccountId()));
		assertFalse(teamMemberRepository.findById(teamMember.getAccountId()).isPresent());
	}

	@Test
	void testAssignToTeam() {
		TeamMember teamMember = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");
		TeamMember teamLead = adminService.createTeamMember("Team Lead", "lead" + System.nanoTime() + "@example.com");

		Team team = teamService.createTeam("Team Name " + System.nanoTime(), teamLead.getAccountId());

		adminService.assignToTeam(teamMember.getAccountId(), team.getTeamId());
		
		TeamMember updatedTeamMember = teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow();
		updatedTeamMember.getTeams().size();

		assertTrue(updatedTeamMember.getTeams().stream()
				.anyMatch(isMember -> isMember.getTeam().getTeamId() == team.getTeamId()));
	}

	@Test
	void testLockTask() {
		adminService.lockTask(unlockedTask.getTaskId());
		Task updatedTask = taskRepository.findById(unlockedTask.getTaskId()).orElseThrow();
		assertTrue(updatedTask.isIsLocked());
	}

	@Test
	void testUnlockTask() {
		adminService.unlockTask(lockedTask.getTaskId());
		Task updatedTask = taskRepository.findById(lockedTask.getTaskId()).orElseThrow();
		assertFalse(updatedTask.isIsLocked());
	}
}
