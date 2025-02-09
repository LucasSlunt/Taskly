package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.TeamMember;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminEntityTest {

    @Autowired
    private TestEntityManager entMan;

    /*
     * Tests if an Admin entity can be persisted and retrieved correctly.
     * Ensures that the saved Admin retains the expected username and email.
     */
    @Test
    void testAdminPersistence() {
        Admin admin = new Admin("Admin User", "admin@example.com");
        entMan.persist(admin);
        entMan.flush();

        Admin savedAdmin = entMan.find(Admin.class, admin.getAccountId());

        assertNotNull(savedAdmin);
        assertEquals("Admin User", savedAdmin.getUserName());
        assertEquals("admin@example.com", savedAdmin.getUserEmail());
    }

    /*
     * Tests whether an Admin entity is also stored as a TeamMember.
     * Ensures that Admin objects are properly recognized as instances of TeamMember.
     */
    @Test
    void testAdminStoredAsTeamMember() {
        Admin admin = new Admin("Admin User", "admin@example.com");
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
    void testAdminQueryFromTeamMember() {
        Admin admin = new Admin("Admin User", "admin@example.com");
        TeamMember teamMember = new TeamMember("Team User", "team_user@example.com");
        entMan.persist(admin);
        entMan.persist(teamMember);
        entMan.flush();

        List<TeamMember> teamMembers = entMan.getEntityManager()
                .createQuery("SELECT tm FROM TeamMember tm", TeamMember.class)
                .getResultList();

        assertEquals(2, teamMembers.size());
        assertTrue(teamMembers.stream().anyMatch(tm -> tm instanceof Admin));

    }
}
