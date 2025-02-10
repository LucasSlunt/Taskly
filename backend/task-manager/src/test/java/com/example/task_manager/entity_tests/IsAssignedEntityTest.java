package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IsAssignedEntityTest {

	@Autowired
    private TestEntityManager entMan;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private IsAssignedRepository isAssignedRepository;


	/**
	 * Tests if an IsAssigned entity can be persisted and retrieved correctly.
	 * Ensures that the saved assignment retains the correct task, team, and team member details.
	 */
	@Test
	void testIsAssignedPersistence() {
		Team team = new Team();
		team.setTeamName("Test Team Name");
		entMan.persist(team);
		entMan.flush();

		Task task = new Task("Test Task Title", "Test description.", team, false, "Open", LocalDate.now());
		entMan.persist(task);
		entMan.flush();

		TeamMember teamMember = new TeamMember("Test User", "test@example.com");
		entMan.persist(teamMember);
		entMan.flush();

		IsAssigned isAssigned = new IsAssigned();
		isAssigned.setTask(task);
		isAssigned.setTeam(team);
		isAssigned.setTeamMember(teamMember);
		entMan.persist(isAssigned);
		entMan.flush();

		IsAssigned savedAssignment = entMan.find(IsAssigned.class, isAssigned.getId());
		assertNotNull(savedAssignment);
		assertEquals("Test Task Title", savedAssignment.getTask().getTitle());
		assertEquals("Test User", savedAssignment.getTeamMember().getUserName());
		assertEquals("Test Team Name", savedAssignment.getTeam().getTeamName());

	}

	/**
	 * Tests whether an IsAssigned entity is deleted when the associated Task is removed.
	 * Ensures that cascading delete behavior works as expected and prevents orphaned assignments.
	 */
	@Test
	void testCascadeDeleteWithTask() {
		Team team = new Team();
		team.setTeamName("Test Team Name");
		teamRepository.save(team);

		Task task = new Task("Task Title" + System.nanoTime(), null, team, false, "Open", LocalDate.now());
		taskRepository.save(task);

		TeamMember teamMember = new TeamMember("TeamMember Name" + System.nanoTime(), System.nanoTime() + "teamMember@example.com");
		teamMemberRepository.save(teamMember);

		IsAssigned isAssigned = new IsAssigned();
		isAssigned.setTask(task);
		isAssigned.setTeam(team);
		isAssigned.setTeamMember(teamMember);

		team.getAssignedTasks().add(isAssigned);
		task.getAssignedMembers().add(isAssigned);
		teamMember.getAssignedTasks().add(isAssigned);

		isAssignedRepository.save(isAssigned);

		entMan.flush();

		teamRepository.delete(team);

		assertNull(entMan.find(Task.class, task.getTaskId()));
		assertNull(entMan.find(IsAssigned.class, isAssigned.getId()));
	}
}
