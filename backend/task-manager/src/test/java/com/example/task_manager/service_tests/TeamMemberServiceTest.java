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

import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.service.TeamMemberService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class TeamMemberServiceTest {

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

	private Task task;
	private TeamMember teamMember;
	private Team team;

	@BeforeEach
	void setUp() {
		isAssignedRepository.deleteAllInBatch();
		taskRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
		teamMemberRepository.deleteAllInBatch();

		teamMember = new TeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");
		teamMember = teamMemberRepository.save(teamMember);

		team = new Team("Team Name " + System.nanoTime(), teamMember);
		team = teamRepository.save(team);

		task = new Task("Task Title " + System.nanoTime(), "Task Description", team, false, "Open", LocalDate.now());
		task = taskRepository.save(task);
	}

	@Test
	void testCreateTask() {
		Task newTask = teamMemberService.createTask("New Task", "Task Description", false, "Open", LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), team, null);

		assertNotNull(newTask);
		assertEquals("New Task", newTask.getTitle());
		assertEquals("Task Description", newTask.getDescription());
		assertEquals(team.getTeamId(), newTask.getTeam().getTeamId());
	}

	@Test
	void testDeleteTask() {
		teamMemberService.deleteTask(task.getTaskId());

		Optional<Task> deletedTask = taskRepository.findById(task.getTaskId());
		assertFalse(deletedTask.isPresent());
	}

	@Test
	void testEditTask() {
		Task updatedTask = teamMemberService.editTask(task.getTaskId(), "Updated Task Title", "Updated Description", true, "In Progress", LocalDate.now().plusDays(7), LocalDate.now().plusDays(10));

		assertEquals("Updated Task Title", updatedTask.getTitle());
		assertEquals("Updated Description", updatedTask.getDescription());
		assertEquals("In Progress", updatedTask.getStatus());
		assertTrue(updatedTask.isIsLocked());
	}

	@Test
	void testAssignToTask() {
		teamMemberService.assignToTask(task.getTaskId(), teamMember.getAccountId());

		TeamMember updatedTeamMember = teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow();
		updatedTeamMember.getAssignedTasks().size();

		assertTrue(updatedTeamMember.getAssignedTasks()
			.stream()
			.anyMatch(t -> t.getTask().getTaskId() == task.getTaskId()));
	}

	@Test
	void testChangePassword() {
		//to be implemented with password methods
	}
}
