package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.TaskDTO;
import com.example.task_manager.DTO.TeamMemberDTO;
import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.service.AuthInfoService;
import com.example.task_manager.service.TeamMemberService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class TeamMemberServiceTest {

	@Autowired
    private AuthInfoService authInfoService;

	@Autowired
	private TeamMemberService teamMemberService;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TeamMemberRepository teamMemberRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private IsAssignedRepository isAssignedRepository;

	@Autowired
    private AuthInfoRepository authInfoRepository;

	private Task task;
	private TeamMember teamMember;
	private Team team;

	@BeforeEach
	void setUp() {
		isAssignedRepository.deleteAllInBatch();
		taskRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
		authInfoRepository.deleteAllInBatch();
		teamMemberRepository.deleteAllInBatch();

		teamMember = new TeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com","defaultpw");
		teamMember = teamMemberRepository.save(teamMember);

		team = new Team("Team Name " + System.nanoTime(), teamMember);
		team = teamRepository.save(team);

		task = new Task("Task Title " + System.nanoTime(), "Task Description", team, false, "Open", LocalDate.now());
		task = taskRepository.save(task);
	}

	@Test
	void testCreateTask() {
		TaskDTO newTaskDTO = teamMemberService.createTask("New Task", "Task Description", false, "Open", LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), team, null);

		assertNotNull(newTaskDTO);
		assertEquals("New Task", newTaskDTO.getTitle());
		assertEquals("Task Description", newTaskDTO.getDescription());
		assertEquals(team.getTeamId(), newTaskDTO.getTeamId());
	}

	@Test
	void testCreateTaskWithNullTitle() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.createTask(null, "Task Description", false, "Open", 
				LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), team, null));

		assertTrue(exception.getMessage().contains("Task title cannot be null"));
	}

	@Test
	void testCreateTaskWithEmptyTitle() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.createTask("", "Task Description", false, "Open", 
				LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), team, null));

		assertTrue(exception.getMessage().contains("Task title cannot be null or empty"));
	}

	@Test
	void testCreateTaskWithNullTeam() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.createTask("New Task", "Task Description", false, "Open", 
				LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), null, null));

		assertTrue(exception.getMessage().contains("Task must be assigned to a team"));
	}

	@Test
	void testDeleteTask() {
		teamMemberService.deleteTask(task.getTaskId());

		Optional<Task> deletedTask = taskRepository.findById(task.getTaskId());
		assertFalse(deletedTask.isPresent());
	}

	@Test
	void testDeleteNonExistentTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.deleteTask(9999));

		assertTrue(exception.getMessage().contains("Task not found"));
	}

	@Test
	void testEditTask() {
		TaskDTO updatedTask = teamMemberService.editTask(task.getTaskId(), "Updated Task Title", "Updated Description", true, "In Progress", LocalDate.now().plusDays(7), LocalDate.now().plusDays(10));

		assertEquals("Updated Task Title", updatedTask.getTitle());
		assertEquals("Updated Description", updatedTask.getDescription());
		assertEquals("In Progress", updatedTask.getStatus());
		assertTrue(updatedTask.isLocked());
	}

	@Test
	void testEditNonExistentTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.editTask(9999, "Updated Title", "Updated Description", true, 
				"In Progress", LocalDate.now().plusDays(7), LocalDate.now().plusDays(10)));

		assertTrue(exception.getMessage().contains("Task not found"));
	}

	@Test
	void testAssignToTask() {
		teamMemberService.assignToTask(task.getTaskId(), teamMember.getAccountId());

		TeamMemberDTO updatedTeamMember = teamMemberService.getTeamMember(teamMember.getAccountId());

		assertTrue(teamMemberService.getAssignedTasks(teamMember.getAccountId())
			.stream()
			.anyMatch(t -> t.getTaskId() == task.getTaskId()));
	}

	@Test
	void testAssignNonExistentMemberToTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.assignToTask(task.getTaskId(), 9999));

		assertTrue(exception.getMessage().contains("Team Member not found"));
	}

	@Test
	void testAssignToNonExistentTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.assignToTask(9999, teamMember.getAccountId()));

		assertTrue(exception.getMessage().contains("Task not found"));
	}

	@Test
	void testAssignMemberToSameTaskTwice() {
		teamMemberService.assignToTask(task.getTaskId(), teamMember.getAccountId());

		Exception exception = assertThrows(RuntimeException.class, 
			() -> teamMemberService.assignToTask(task.getTaskId(), teamMember.getAccountId()));

		assertTrue(exception.getMessage().contains("Team Member is already assigned"));
	}

	@Test
	void testGetAssignedTasksForNonExistentMember() {
		assertTrue(teamMemberService.getAssignedTasks(9999).isEmpty());
	}

	@Test
	void testChangePassword() {
		int teamMemberId = teamMember.getAccountId();
		teamMemberService.changePassword(teamMemberId, "defaultpw", "coolnewpassword");
		assertTrue(authInfoService.approveLogin(teamMemberId,"coolnewpassword"));
	}

	@Test
	void testChangePasswordButSupplyWrongPassword() {
		int teamMemberId = teamMember.getAccountId();
		teamMemberService.changePassword(teamMemberId, "wrongpw", "coolnewpassword");
		assertFalse(authInfoService.approveLogin(teamMemberId,"coolnewpassword"));
	}

    @Test
 	void testIsAdmin() {
 		Admin admin = new Admin("Admin Name" + System.nanoTime(), "admin_email_" + System.nanoTime(), "admin_password");
 		admin = teamMemberRepository.save(admin);
 		boolean isAdmin = authInfoService.isAdmin(admin.getAccountId());
 
 		assertTrue(isAdmin);
 	}
 
 	//testing a team member is not an admin, should be false
 	@Test
 	void testIsNotAdmin() {
 		TeamMember teamMember = new TeamMember("Team Member " + System.nanoTime(),
 				"team_member_email_" + System.nanoTime(), "password");
 		teamMember = teamMemberRepository.save(teamMember);
 
 		boolean isAdmin = authInfoService.isAdmin(teamMember.getAccountId());
 
 		assertFalse(isAdmin);
 	}
 	
 	@Test
 	void testIsAdminForNonExistentMember() {
 		int fakeId = 999999; // Assuming this ID does not exist
 
 		Exception exception = assertThrows(RuntimeException.class, () -> {
            authInfoService.isAdmin(fakeId);
 		});
 
 		assertTrue(exception.getMessage().contains("Team Member not found"));
 	}
}
