package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.AuthInfo;
import com.example.task_manager.entity.TeamMember;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AuthInfoEntityTest {

    @Autowired
    private TestEntityManager entMan;

    @BeforeEach
	void cleanDatabase() {
        entMan.getEntityManager().createQuery("DELETE FROM AuthInfo").executeUpdate();
		entMan.getEntityManager().createQuery("DELETE FROM IsAssigned").executeUpdate();
		entMan.getEntityManager().createQuery("DELETE FROM Task").executeUpdate();
		entMan.getEntityManager().createQuery("DELETE FROM Team").executeUpdate();
		entMan.getEntityManager().createQuery("DELETE FROM TeamMember").executeUpdate();
		entMan.flush();
	}

    /**
    * Tests if an AuthInfo entity can be persisted and retrieved correctly.
    * Ensures that the saved AuthInfo retains the correct account ID, hashed password, and salt.
    */
    @Test
    void testAuthInfoPersistence() {
        TeamMember teamMember = new TeamMember(
            "TeamMember" + System.nanoTime(), 
            "team_member" + System.nanoTime() + "@example.com",
            "passwordplease"
        );
        entMan.persist(teamMember);
        entMan.flush();

        AuthInfo authInfo = new AuthInfo("this is literally unique u twat", "this too argh", teamMember);
        entMan.persist(authInfo);
        entMan.flush();

        AuthInfo savedAuthInfo = entMan.find(AuthInfo.class, teamMember.getAccountId());

        assertNotNull(savedAuthInfo);
        assertEquals(teamMember.getAccountId(), savedAuthInfo.getAccountId());
        assertEquals("normalwords", savedAuthInfo.getHashedPassword());
        assertEquals("balls2", savedAuthInfo.getSalt());
    }

    /**
     * Tests that an AuthInfo entity cannot be persisted without a corresponding TeamMember.
     * Ensures that a PersistenceException is thrown when attempting to save AuthInfo without an owner.
     */
    @Test
    void testAuthInfoFailsWithoutTeamMember() {
        AuthInfo authInfo = new AuthInfo("hashed_password2", "random_salt", null);

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
        TeamMember teamMember = new TeamMember("Auth User", "auth@example.com","password2");
        entMan.persist(teamMember);
        entMan.flush();

        AuthInfo authInfo = new AuthInfo("hashed_password3", "random_salt", teamMember);
        teamMember.setAuthInfo(authInfo);
        entMan.persist(authInfo);
        entMan.flush();
        
        entMan.remove(teamMember);
        entMan.flush();

        AuthInfo deletedAuthInfo = entMan.find(AuthInfo.class, authInfo.getAccountId());
        assertNull(deletedAuthInfo);
    }

    /*
     * Tests that AuthInfo is connected to the correct TeamMember
     */
    @Test
    void testAuthInfoBelongsToCorrectTeamMember() {
        TeamMember member1 = new TeamMember("User1", "user1@example.com","user1pw");
        TeamMember member2 = new TeamMember("User2", "user2@example.com","user2pw");

        entMan.persist(member1);
        entMan.persist(member2);
        entMan.flush();

        AuthInfo authInfo1 = new AuthInfo("password1", "salt1", member1);
        AuthInfo authInfo2 = new AuthInfo("password2", "salt2", member2);

        entMan.persist(authInfo1);
        entMan.persist(authInfo2);
        entMan.flush();

        AuthInfo retrievedAuthInfo1 = entMan.find(AuthInfo.class, member1.getAccountId());
        AuthInfo retrievedAuthInfo2 = entMan.find(AuthInfo.class, member2.getAccountId());

        assertNotNull(retrievedAuthInfo1);
        assertNotNull(retrievedAuthInfo2);
        assertEquals(member1.getAccountId(), retrievedAuthInfo1.getAccountId());
        assertEquals(member2.getAccountId(), retrievedAuthInfo2.getAccountId());
    }
}
