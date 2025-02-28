package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.TeamMember;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AdminEntityTest {

	@Autowired
	private TestEntityManager entMan;

	//make sure database is clear before testing -> was running into issues of the database containing data from previous tests
	@BeforeEach
	void cleanDatabase() {
		entMan.getEntityManager().createQuery("DELETE FROM IsAssigned").executeUpdate();
		entMan.getEntityManager().createQuery("DELETE FROM Task").executeUpdate();
		entMan.getEntityManager().createQuery("DELETE FROM Team").executeUpdate();
		entMan.getEntityManager().createQuery("DELETE FROM TeamMember").executeUpdate();
		entMan.flush();
	}

	/*
	 * Tests if an Admin entity can be persisted and retrieved correctly.
	 * Ensures that the saved Admin retains the expected username and email.
	 */
	@Test
	void testAdminPersistence() {
		// Admin admin = new Admin("Admin User TESTING", "adminTESTING@example.com");
		Admin admin = new Admin("Admin Testing", "adminTesting@example.com","defaultpw");
		entMan.persist(admin);
		entMan.flush();

		Admin savedAdmin = entMan.find(Admin.class, admin.getAccountId());

		assertNotNull(savedAdmin);
		assertEquals("Admin Testing", savedAdmin.getUserName());
		assertEquals("adminTesting@example.com", savedAdmin.getUserEmail());
	}

	/*
     * Tests whether an Admin entity is also stored as a TeamMember.
     * Ensures that Admin objects are properly recognized as instances of TeamMember.
     */
	@Test
	void testAdminStoredAsTeamMember() {
		Admin admin = new Admin("Admin" + System.nanoTime(), "admin" + System.nanoTime() + "@example.com","defaultpw");
		entMan.persist(admin);
		entMan.flush();

		TeamMember savedMember = entMan.find(TeamMember.class, admin.getAccountId());

		assertNotNull(savedMember);
		assertTrue(savedMember instanceof Admin);
	}

	 /*
     * Tests whether an Admin can be retrieved when querying all TeamMembers.
     * Ensures that when querying TeamMember entities, Admins are included in the result set.
     */
	@Test
	@Transactional
	@Rollback
	void testAdminQueryFromTeamMember() {
		Admin admin = new Admin("Admin" + System.nanoTime(), "admin" + System.nanoTime() + "@example.com","defaultpw");
		TeamMember teamMember = new TeamMember("TeamUser" + System.nanoTime(), "team_user" + System.nanoTime() + "@example.com","defaultpw");
		entMan.persist(admin);
		entMan.persist(teamMember);
		entMan.flush();

		List<TeamMember> teamMembers = entMan.getEntityManager()
				.createQuery("SELECT tm FROM TeamMember tm", TeamMember.class)
				.getResultList();

		assertEquals(2, teamMembers.size());
		assertTrue(teamMembers.stream().anyMatch(tm -> tm instanceof Admin));
	}

	/*
	 * Ensuring that deleting an admin works just like deleting a teamMember
	 */
	@Test
	void testAdminDeletion() {
		Admin admin = new Admin("AdminToDelete", "deleteadmin@example.com","defaultpw");
		entMan.persist(admin);
		entMan.flush();

		entMan.remove(admin);
		entMan.flush();

		Admin deletedAdmin = entMan.find(Admin.class, admin.getAccountId());
		assertNull(deletedAdmin);
	}

	/*
	 * test that admin can be found with id
	 */
	@Test
	void testAdminQueryById() {
		Admin admin = new Admin("AdminLookup", "lookupadmin@example.com","defaultpw");
		entMan.persist(admin);
		entMan.flush();

		Admin foundAdmin = entMan.getEntityManager()
			.createQuery("SELECT a FROM Admin a WHERE a.accountId = :id", Admin.class)
			.setParameter("id", admin.getAccountId())
			.getSingleResult();

		assertNotNull(foundAdmin);
		assertEquals(admin.getUserEmail(), foundAdmin.getUserEmail());
	}

	/*
	 * Ensure Admin still functions as a teammmember (because it extends TeamMember)
	 */
	@Test
	void testAdminInheritsTeamMemberBehavior() {
		Admin admin = new Admin("InheritedAdmin", "inheritadmin@example.com","defaultpw");
		entMan.persist(admin);
		entMan.flush();

		TeamMember foundMember = entMan.find(TeamMember.class, admin.getAccountId());

		assertNotNull(foundMember);
		assertTrue(foundMember instanceof Admin);
	}
}
