package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

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
import com.example.task_manager.service.IsAssignedService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class IsAssignedServiceTest {

	@Autowired
	private IsAssignedService isAssignedService;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TeamMemberRepository teamMemberRepository;

	@Autowired
	private TeamRepository teamRepository;

	private Task task;
	private TeamMember teamMember;
	private Team team;

	@BeforeEach
	void setUp() {
		taskRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
		teamMemberRepository.deleteAllInBatch();

		teamMember = new TeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com");
		teamMember = teamMemberRepository.save(teamMember);

		team = new Team("Team Name " + System.nanoTime(), teamMember);
		team = teamRepository.save(team);

		task = new Task("Task Title " + System.nanoTime(), "", team, false, "Open", LocalDate.now());
		task = taskRepository.save(task);
	}

	@Test
	void testAssignToTask() {
		isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId());

		teamMemberRepository.flush();
		teamMember = teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow();

		System.out.println("Assigned Tasks (AFTER assignment): " + teamMember.getAssignedTasks());

		assertTrue(teamMember.getAssignedTasks()
			.stream()
			.anyMatch(isAssigned -> isAssigned.getTask().getTaskId() == task.getTaskId())); 
	}


	@Test
	void testUnassignFromTask() {
		isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId());
		isAssignedService.unassignFromTask(teamMember.getAccountId(), task.getTaskId());

		TeamMember updatedTeamMember = teamMemberRepository.findById(teamMember.getAccountId()).orElseThrow();
		updatedTeamMember.getAssignedTasks().size();

		assertFalse(updatedTeamMember.getAssignedTasks()
			.stream()
			.anyMatch(t -> t.getId() == task.getTaskId()));
	}

	@Test
	void testIsAssignedToTask() {
		isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId());

		boolean isAssigned = isAssignedService.isAssignedToTask(teamMember.getAccountId(), task.getTaskId());
		assertTrue(isAssigned);
	}

	@Test
	void testIsNotAssignedToTask() {
		boolean isAssigned = isAssignedService.isAssignedToTask(teamMember.getAccountId(), task.getTaskId());
		assertFalse(isAssigned);
	}
}
