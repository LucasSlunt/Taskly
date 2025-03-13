package com.example.task_manager.service_tests;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
import com.example.task_manager.DTO.TaskRequestDTO;
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
		isAssignedRepository.deleteAll();
		isMemberOfRepository.deleteAll();
		taskRepository.deleteAll();
		teamMemberRepository.deleteAll();
		authInfoRepository.deleteAll();
		adminRepository.deleteAll();
		teamRepository.deleteAll();

		admin = adminService.createAdmin("Admin Name", "admin" + System.nanoTime() + "@example.com","defaultpw");
		teamMember = adminService.createTeamMember("TM Name", "teamMember" + System.nanoTime() + "@example.com","defaultpw");

		team = new Team("Team Name " + System.nanoTime(), teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow());
		team = teamRepository.save(team);


		TaskRequestDTO taskRequest = new TaskRequestDTO(
			"Unlocked Task",
			"Task Description",
			false,
			"Open",
			LocalDate.now(),
			null,
			team.getTeamId()
		);

		unlockedTask = adminService.createTask(taskRequest);

		taskRequest = new TaskRequestDTO(
			"Locked Task",
			"Task Description",
			true,
			"Open",
			LocalDate.now(),
			null,
			team.getTeamId()
		);

		lockedTask = adminService.createTask(taskRequest);

		adminRepository.save(new Admin("Alice Chains", "alice@example.com", "alice_password"));
		adminRepository.save(new Admin("Rooster", "rooster@example.com", "rooster_password"));
		adminRepository.save(new Admin("Pink Floyd", "pink@example.com", "pink_password"));
	}

	// Try to create admin account
	@Test
	void testCreateAdmin() {
		AdminDTO adminDTO = adminService.createAdmin("Admin Name Testing", "admin" + System.nanoTime() + "@example.com","defaultpw");

		assertNotNull(adminDTO);
		assertEquals("Admin Name Testing", adminDTO.getUserName());
	}	

	// try to create admin with the same name
	@Test
	void testCreateAdminExistingName() {
		AdminDTO adminDTO = adminService.createAdmin("John Doe", "admin" + System.nanoTime() + "@example.com","defaultpw");

		Exception exception = assertThrows(RuntimeException.class,
				() -> adminService.createAdmin("John Doe", "admin" + System.nanoTime() + "@example.com","defaultpw"));

		assertNotNull(adminDTO);
	}
	
	// try to create admin with the same email
	@Test
	void testCreateAdminExistingEmail() { 
		AdminDTO adminDTO = adminService.createAdmin("John Doe", "admin_email@example.com","defaultpw");

		Exception exception = assertThrows(RuntimeException.class,
				() -> adminService.createAdmin("Alice Wonder", "admin_email@example.com","defaultpw"));

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
		TeamMemberDTO teamMemberDTO = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com","defaultpw");

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
		TeamMemberDTO newTeamMember = adminService.createTeamMember("Team Member Email Test", "teamMemberEmailTest@example.com","defaultpw");

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
	
    //to be re-implemented with hashedPassword and login functionality
	// @Test
	// void testPromoteToAdmin() {
	// 	TeamMemberDTO teamMember = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");
	// 	AdminDTO updatedAdmin = adminService.promoteToAdmin(teamMember.getAccountId());

	// 	assertTrue(adminRepository.existsById(updatedAdmin.getAccountId()));
	// 	assertFalse(teamMemberRepository.findById(teamMember.getAccountId()).isPresent());
	// }

	@Test
	void testAssignToTeam() {
		TeamMemberDTO teamMember = adminService.createTeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com","defaultpw");
		TeamMemberDTO teamLead = adminService.createTeamMember("Team Lead", "lead" + System.nanoTime() + "@example.com","defaultpw");

		TeamDTO team = teamService.createTeam("Team Name " + System.nanoTime(), teamLead.getAccountId());

		adminService.assignToTeam(teamMember.getAccountId(), team.getTeamId());
		
		TeamMember updatedTeamMember = teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow();
		updatedTeamMember.getTeams().size();

		assertTrue(updatedTeamMember.getTeams().stream()
				.anyMatch(isMember -> isMember.getTeam().getTeamId() == team.getTeamId()));
	}

	@Test
	void testAssignToNonexistentTeam() {
		TeamMemberDTO teamMember = adminService.createTeamMember("Team Member", "test@example.com","defaultpw");

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

	@Test
	void testGetAllAdmins() {

		List<AdminDTO> result = adminService.getAllAdmins();

		assertEquals(4, result.size());
		assertEquals("Alice Chains", result.get(1).getUserName());
		assertEquals("pink@example.com", result.get(3).getUserEmail());
	}
	
	@Test
	void testGetAllTeamMembers() {

		List<TeamMemberDTO> result = adminService.getAllTeamMembers();

		assertEquals(4, result.size());
		assertEquals("Alice Chains", result.get(1).getUserName());
		assertEquals("pink@example.com", result.get(3).getUserEmail());
	}

	@Test
	void getAllTeams() {
		List<TeamDTO> result = adminService.getAllTeams();

		assertEquals(1, result.size());
		assertEquals(team.getTeamId(), result.get(0).getTeamId());
		assertEquals(team.getTeamName(), result.get(0).getTeamName());
	}

	// Getting admin by ID
	@Test
	void getAdminById() throws Exception {
		Admin admin = new Admin("Test Admin", "admin@example.com", "password");
		admin = adminRepository.save(admin);

		AdminDTO result = adminService.getAdminById(admin.getAccountId());

		assertNotNull(result);
		assertEquals(admin.getAccountId(), result.getAccountId());
		assertEquals("Test Admin", result.getUserName());
	}

	//get team member by ID
	@Test
	void testGetTeamMemberById() {
		TeamMember teamMember = new TeamMember("Test TM", "tm@example.com", "password");
		teamMember = teamMemberRepository.save(teamMember);

		TeamMemberDTO result = adminService.getTeamMemberById(teamMember.getAccountId());

		assertNotNull(result);
		assertEquals(teamMember.getAccountId(), result.getAccountId());
		assertEquals("Test TM", result.getUserName());
	}
}
