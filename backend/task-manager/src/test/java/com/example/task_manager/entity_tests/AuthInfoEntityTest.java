package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.AuthInfo;
import com.example.task_manager.entity.TeamMember;

import jakarta.persistence.PersistenceException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthInfoEntityTest {

    @Autowired
    private TestEntityManager entMan;

    /**
    * Tests if an AuthInfo entity can be persisted and retrieved correctly.
    * Ensures that the saved AuthInfo retains the correct account ID, hashed password, and salt.
    */
    @Test
    void testAuthInfoPersistence() {
        TeamMember teamMember = new TeamMember(
            "TeamMember" + System.nanoTime(), 
            "team_member" + System.nanoTime() + "@example.com"
        );
        entMan.persist(teamMember);
        entMan.flush();

        AuthInfo authInfo = new AuthInfo("hashed_password123", "random_salt", teamMember);
        entMan.persist(authInfo);
        entMan.flush();

        AuthInfo savedAuthInfo = entMan.find(AuthInfo.class, teamMember.getAccountId());

        assertNotNull(savedAuthInfo);
        assertEquals(teamMember.getAccountId(), savedAuthInfo.getAccountId());
        assertEquals("hashed_password123", savedAuthInfo.getHashedPassword());
        assertEquals("random_salt", savedAuthInfo.getSalt());
    }

    /**
     * Tests that an AuthInfo entity cannot be persisted without a corresponding TeamMember.
     * Ensures that a PersistenceException is thrown when attempting to save AuthInfo without an owner.
     */
    @Test
    void testAuthInfoFailsWithoutTeamMember() {
        AuthInfo authInfo = new AuthInfo("hashed_password123", "random_salt", null);

        Exception e = assertThrows(PersistenceException.class, () -> {
            entMan.persist(authInfo);
            entMan.flush();
        });

        assertNotNull(e);
    }

    /**
     * Tests whether an AuthInfo entity is deleted when its associated TeamMember is removed.
     * Ensures that cascading delete behavior works as expected, preventing orphaned AuthInfo records.
     */
    @Test
    void testAuthInfoIsDeletedWithTeamMember() {
        TeamMember teamMember = new TeamMember("Auth User", "auth@example.com");
        entMan.persist(teamMember);
        entMan.flush();

        AuthInfo authInfo = new AuthInfo("hashed_password123", "random_salt", teamMember);
        teamMember.setAuthInfo(authInfo);
        entMan.persist(authInfo);
        entMan.flush();
        
        entMan.remove(teamMember);
        entMan.flush();

        AuthInfo deletedAuthInfo = entMan.find(AuthInfo.class, authInfo.getAccountId());
        assertNull(deletedAuthInfo);
    }
}
