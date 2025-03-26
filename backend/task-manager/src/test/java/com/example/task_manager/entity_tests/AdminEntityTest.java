package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.TeamMember;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ActiveProfiles("test")
public class AdminEntityTest extends EntityTestHelper{
    /**
     * Tests if an Admin entity can be persisted and retrieved correctly.
     */
    @Test
    void testAdminPersistence() {
        Admin admin = createUniqueAdmin();
        entityManager.persist(admin);
        entityManager.flush();

        Admin savedAdmin = entityManager.find(Admin.class, admin.getAccountId());

        assertNotNull(savedAdmin);
        assertEquals(admin.getUserName(), savedAdmin.getUserName());
        assertEquals(admin.getUserEmail(), savedAdmin.getUserEmail());
    }

    /**
     * Tests whether an Admin entity is also stored as a TeamMember.
     */
    @Test
    void testAdminStoredAsTeamMember() {
        Admin admin = createUniqueAdmin();
        entityManager.persist(admin);
        entityManager.flush();

        TeamMember savedMember = entityManager.find(TeamMember.class, admin.getAccountId());

        assertNotNull(savedMember);
        assertTrue(savedMember instanceof Admin);
    }

    /**
     * Tests whether an Admin can be retrieved when querying all TeamMembers.
     */
    @Test
    @Transactional
    @Rollback
    void testAdminQueryFromTeamMember() {
        Admin admin = createUniqueAdmin();
        TeamMember teamMember = createUniqueTeamMember();
        entityManager.persist(admin);
        entityManager.persist(teamMember);
        entityManager.flush();

        List<TeamMember> teamMembers = entityManager.getEntityManager()
                .createQuery("SELECT tm FROM TeamMember tm", TeamMember.class)
                .getResultList();

        assertTrue(teamMembers.stream().anyMatch(tm -> tm instanceof Admin));
    }

    /**
     * Ensuring that deleting an admin works just like deleting a teamMember.
     */
    @Test
    void testAdminDeletion() {
        Admin admin = createUniqueAdmin();
        entityManager.persist(admin);
        entityManager.flush();

        entityManager.remove(admin);
        entityManager.flush();

        Admin deletedAdmin = entityManager.find(Admin.class, admin.getAccountId());
        assertNull(deletedAdmin);
    }

    /**
     * Test that admin can be found by ID.
     */
    @Test
    void testAdminQueryById() {
        Admin admin = createUniqueAdmin();
        entityManager.persist(admin);
        entityManager.flush();

        Admin foundAdmin = entityManager.getEntityManager()
                .createQuery("SELECT a FROM Admin a WHERE a.accountId = :id", Admin.class)
                .setParameter("id", admin.getAccountId())
                .getSingleResult();

        assertNotNull(foundAdmin);
        assertEquals(admin.getUserEmail(), foundAdmin.getUserEmail());
    }

    /**
     * Ensure Admin still functions as a TeamMember (since it extends TeamMember).
     */
    @Test
    void testAdminInheritsTeamMemberBehavior() {
        Admin admin = createUniqueAdmin();
        entityManager.persist(admin);
        entityManager.flush();

        TeamMember foundMember = entityManager.find(TeamMember.class, admin.getAccountId());

        assertNotNull(foundMember);
        assertTrue(foundMember instanceof Admin);
    }
}
