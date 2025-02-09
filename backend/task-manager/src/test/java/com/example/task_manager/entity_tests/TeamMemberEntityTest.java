package com.example.task_manager.entity_tests;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.AuthInfo;
import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;

import jakarta.persistence.PersistenceException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //tells spring to NOT use the database in memory (so we can test with h2)
public class TeamMemberEntityTest {

	@Autowired
	private TestEntityManager entityManager;

	/**
     * Tests if a TeamMember entity can be persisted and retrieved correctly.
     * Ensures that the saved member retains the correct name and email.
     */
	@Test
	void testTeamMemberPersistence() {
		TeamMember teamMember = new TeamMember("John Doe", "john.doe@example.com"); //creating team member object which will be saved to the database

		TeamMember savedMember = entityManager.persistFlushFind(teamMember); //saves teamMember entity to the database, flushes changes, then checks if it was saved to database properly

		assertNotNull(savedMember.getAccountId());
		assertEquals("John Doe", savedMember.getUserName());
		assertEquals("john.doe@example.com", savedMember.getUserEmail());
	}

	/**
     * Tests whether the unique constraint on the user name is enforced.
     * Ensures that attempting to save two TeamMembers with the same name results in a PersistenceException.
     */
	@Test
	void testUniqueNameConstraint() {
		TeamMember teamMember_1 = new TeamMember("Unique Name", "unique_1@example.com");
		entityManager.persist(teamMember_1);
		entityManager.flush();

		TeamMember teamMember_2 = new TeamMember("Unique Name", "unique_2@example.com");

		Exception e = assertThrows(PersistenceException.class, () -> {
			entityManager.persist(teamMember_2);
			entityManager.flush();
		});

		assertNotNull(e);
	}

	/**
     * Tests whether the unique constraint on the user email is enforced.
     * Ensures that attempting to save two TeamMembers with the same email results in a PersistenceException.
     */
	@Test
	void testUniqueEmailConstraint() {
		TeamMember member_1 = new TeamMember("Unique One", "unique@example.com");
		entityManager.persist(member_1);
		entityManager.flush();

		TeamMember member_2 = new TeamMember("Unique Two", "unique@example.com");

		Exception e = assertThrows(PersistenceException.class, () -> {
			entityManager.persist(member_2);
			entityManager.flush();
		});

		assertNotNull(e);
	}

	/**
     * Tests the relationship between TeamMember and AuthInfo.
     * Ensures that when a TeamMember is persisted, its associated AuthInfo is also persisted correctly.
     */
	@Test
	void testAuthInfoRelation() {
		TeamMember teamMember = new TeamMember("Auth User", "auth@example.com");
		AuthInfo authInfo = new AuthInfo();

		authInfo.setHashedPassword("secure_password");
		authInfo.setSalt("random_salt");
		authInfo.setTeamMember(teamMember);

		teamMember.setAuthInfo(authInfo);

		TeamMember savedMember = entityManager.persistFlushFind(teamMember);

		assertNotNull(savedMember.getAuthInfo());
		assertEquals("secure_password", savedMember.getAuthInfo().getHashedPassword());
	}

	/**
     * Tests the relationship between TeamMember and IsMemberOf.
     * Ensures that when a TeamMember joins a Team, the association is correctly established.
     */
	@Test
	void testIsMemberOfRelation() {
		Team team = new Team();
		team.setTeamName("Test Team Name");
		entityManager.persist(team);
		entityManager.flush();

		TeamMember teamMember = new TeamMember("John Doe", "john@example.com");
		entityManager.persist(teamMember);
		entityManager.flush();

		IsMemberOf isMemberOf = new IsMemberOf();
		isMemberOf.setTeamMember(teamMember);
		isMemberOf.setTeam(team);

		entityManager.persist(isMemberOf);
		entityManager.flush();

		teamMember.getTeams().add(isMemberOf);

		TeamMember savedMember = entityManager.persistFlushFind(teamMember);
		assertEquals(1, savedMember.getTeams().size());
	}

	/**
     * Tests the relationship between TeamMember and IsAssigned.
     * Ensures that when a task is assigned to a TeamMember, the assignment is properly stored and retrieved.
     */
	@Test
	void testIsAssignedTasksRelation() {
		TeamMember teamMember = new TeamMember("User Task", "user@example.com");
		entityManager.persist(teamMember);
		entityManager.flush();

		Team team = new Team();
		team.setTeamName("Test Team");
		entityManager.persist(team);
		entityManager.flush();
		
		Task task = new Task("Test Task Title", "Test Description", team, false, "Open", LocalDate.now());
		entityManager.persist(task);
		entityManager.flush();

		IsAssigned assignedTask = new IsAssigned();
		assignedTask.setTeamMember(teamMember);
		assignedTask.setTeam(team);
		assignedTask.setTask(task);

		entityManager.persist(assignedTask);
		entityManager.flush();

		teamMember.setAssignedTasks(Set.of(assignedTask));

		TeamMember savedMember = entityManager.persistFlushFind(teamMember);

		assertEquals(1, savedMember.getAssignedTasks().size());
	}
}
