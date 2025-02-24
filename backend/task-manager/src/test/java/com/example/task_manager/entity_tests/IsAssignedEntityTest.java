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

	/*
	 * Testing IsAssigned constructor
	 */
	@Test
	void testIsAssignedDefaultConstructor() {
		IsAssigned isAssigned = new IsAssigned();
		assertNotNull(isAssigned);
	}

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

		Task task = new Task("Task Title" + System.nanoTime(), "Task Description", team, false, "Open", LocalDate.now());
		
		team.getTasks().add(task);
		task.setTeam(team);
		taskRepository.save(task);

		TeamMember teamMember = new TeamMember("TeamMember Name" + System.nanoTime(), System.nanoTime() + "teamMember@example.com");
		teamMemberRepository.save(teamMember);

		IsAssigned isAssigned = new IsAssigned(task, teamMember, team);

		team.getAssignedTasks().add(isAssigned);
		task.getAssignedMembers().add(isAssigned);
		teamMember.getAssignedTasks().add(isAssigned);

		isAssignedRepository.save(isAssigned);

		entMan.flush();

		teamRepository.delete(team);

		assertNull(entMan.find(Task.class, task.getTaskId()));
		assertNull(entMan.find(IsAssigned.class, isAssigned.getId()));
	}

	/*
	 * tests that if TeamMember is deleted so are their IsAssigned associations
	 */
	@Test
	void testCascadeDeleteWithTeamMember() {
		Team team = new Team();
		team.setTeamName("Team with Member");
		entMan.persist(team);
		entMan.flush();

		Task task = new Task("Task for Member", "Description", team, false, "Open", LocalDate.now());
		entMan.persist(task);
		entMan.flush();

		TeamMember teamMember = new TeamMember("User To Remove", "remove@example.com");
		entMan.persist(teamMember);
		entMan.flush();

		IsAssigned isAssigned = new IsAssigned(task, teamMember, team);

		teamMember.getAssignedTasks().add(isAssigned);
		entMan.persist(isAssigned);
		entMan.flush();

		entMan.remove(teamMember);
		entMan.flush();

		assertNull(entMan.find(IsAssigned.class, isAssigned.getId()));
	}

	/*
	 * Testing that when Team is deleted, all associated IsAssigned associations are also deleted
	 */
	@Test
	void testCascadeDeleteWithTeam() {
		Team team = new Team();
		team.setTeamName("Delete Team");
		entMan.persist(team);
		entMan.flush();

		Task task = new Task("Task in Deleted Team", "Description", team, false, "Open", LocalDate.now());
		entMan.persist(task);
		entMan.flush();

		TeamMember teamMember = new TeamMember("Member of Deleted Team", "deletedmember@example.com");
		entMan.persist(teamMember);
		entMan.flush();

		IsAssigned isAssigned = new IsAssigned(task, teamMember, team);

		team.getAssignedTasks().add(isAssigned);
		entMan.persist(isAssigned);
		entMan.flush();

		entMan.remove(team);

		assertNull(entMan.find(IsAssigned.class, isAssigned.getId()));
	}

	/*
	 * When IsAssigned is deleted, no other entities are affected
	 */
	@Test
	void testRemoveIsAssignedWithoutAffectingEntities() {
		Team team = new Team();
		team.setTeamName("Independent Team");
		entMan.persist(team);
		entMan.flush();

		Task task = new Task("Standalone Task", "Task that should remain", team, false, "Open", LocalDate.now());
		entMan.persist(task);
		entMan.flush();

		TeamMember teamMember = new TeamMember("Independent Member", "independent@example.com");
		entMan.persist(teamMember);
		entMan.flush();

		IsAssigned isAssigned = new IsAssigned(task, teamMember, team);
		entMan.persist(isAssigned);
		entMan.flush();

		entMan.remove(isAssigned);
		entMan.flush();

		assertNotNull(entMan.find(Task.class, task.getTaskId()));
		assertNotNull(entMan.find(Team.class, team.getTeamId()));
		assertNotNull(entMan.find(TeamMember.class, teamMember.getAccountId()));
	}
}
