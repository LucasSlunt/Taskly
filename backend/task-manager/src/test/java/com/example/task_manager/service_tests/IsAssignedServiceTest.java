package com.example.task_manager.service_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import com.example.task_manager.DTO.IsAssignedDTO;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.AuthInfoRepository;
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

	@Autowired
    private AuthInfoRepository authInfoRepository;

	private Task task;
	private TeamMember teamMember;
	private Team team;

	@BeforeEach
	void setUp() {
		taskRepository.deleteAllInBatch();
		teamRepository.deleteAllInBatch();
		authInfoRepository.deleteAllInBatch();
		teamMemberRepository.deleteAllInBatch();

		teamMember = new TeamMember("Team Member", "teamMember" + System.nanoTime() + "@example.com","defaultpw");
		teamMember = teamMemberRepository.save(teamMember);

		team = new Team("Team Name " + System.nanoTime(), teamMember);
		team = teamRepository.save(team);

		task = new Task("Task Title " + System.nanoTime(), "", team, false, "Open", LocalDate.now());
		task = taskRepository.save(task);
	}

	@Test
	void testAssignToTask() {
		IsAssignedDTO isAssignedDTO = isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId());

		assertNotNull(isAssignedDTO);
		assertEquals(teamMember.getAccountId(), isAssignedDTO.getTeamMemberId());
		assertEquals(task.getTaskId(), isAssignedDTO.getTaskId());
	}


	@Test
	void testUnassignFromTask() {
		isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId());
        isAssignedService.unassignFromTask(teamMember.getAccountId(), task.getTaskId());

        boolean isAssigned = isAssignedService.isAssignedToTask(teamMember.getAccountId(), task.getTaskId());
        assertFalse(isAssigned);
	}

	@Test
	void testAssignToNonExistentTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> isAssignedService.assignToTask(teamMember.getAccountId(), 9999));

		assertTrue(exception.getMessage().contains("Task not found"));
	}

	@Test
	void testAssignNonExistentTeamMemberToTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> isAssignedService.assignToTask(9999, task.getTaskId()));

		assertTrue(exception.getMessage().contains("Team Member not found"));
	}

	@Test
	void testAssignTeamMemberToSameTaskTwice() {
		isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId());

		Exception exception = assertThrows(RuntimeException.class, 
			() -> isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId()));

		assertTrue(exception.getMessage().contains("Team Member is already assigned"));
	}

	@Test
	void testUnassignTeamMemberNotAssignedToTask() {
		Exception exception = assertThrows(RuntimeException.class, 
			() -> isAssignedService.unassignFromTask(teamMember.getAccountId(), task.getTaskId()));

		assertTrue(exception.getMessage().contains("Assignment not found"));
	}

	@Test
	void testUnassignFromNonExistentTask() {
		isAssignedService.assignToTask(teamMember.getAccountId(), task.getTaskId());

		Exception exception = assertThrows(RuntimeException.class, 
			() -> isAssignedService.unassignFromTask(teamMember.getAccountId(), 9999));

		assertTrue(exception.getMessage().contains("Task not found"));
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

	@Test
	void testIsAssignedForNonExistentTask() {
		boolean isAssigned = isAssignedService.isAssignedToTask(teamMember.getAccountId(), 9999);
		assertFalse(isAssigned);
	}

	@Test
	void testIsAssignedForNonExistentTeamMember() {
		boolean isAssigned = isAssignedService.isAssignedToTask(9999, task.getTaskId());
		assertFalse(isAssigned);
	}
}
