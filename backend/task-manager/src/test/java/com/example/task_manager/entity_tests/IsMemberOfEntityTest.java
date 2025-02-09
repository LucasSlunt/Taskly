package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //tells spring to NOT use the database in memory (so we can test with h2)
public class IsMemberOfEntityTest {

    @Autowired
    private TestEntityManager entMan;

    /**
     * Tests if an IsMemberOf entity can be persisted and retrieved correctly.
     * Ensures that the saved entity retains the correct association between a team member and a team.
     */
    @Test
    void testIsMemberOfPersistence() {
        TeamMember teamMember = new TeamMember("Test Member", "test@example.com");
        entMan.persist(teamMember);
        entMan.flush();

        Team team = new Team("Test Team Name", teamMember);
        entMan.persist(team);
        entMan.flush();

        IsMemberOf isMember = new IsMemberOf(teamMember, team);
        entMan.persist(isMember);
        entMan.flush();

        IsMemberOf savedIsMember = entMan.find(IsMemberOf.class, isMember.getId());
        assertNotNull(savedIsMember);
        assertEquals("Test Member", savedIsMember.getTeamMember().getUserName());
        assertEquals("Test Team Name", savedIsMember.getTeam().getTeamName());

    }

    /**
     * Tests whether an IsMemberOf entity is deleted when the associated TeamMember is removed.
     * Ensures cascading delete behavior works as expected and prevents orphaned IsMemberOf records.
     */
    @Test
    void testCascadeDeleteIsMember() {
        TeamMember teamMember = new TeamMember("Test Member", "test@example.com");
        entMan.persist(teamMember);
        entMan.flush();

        Team team = new Team("Test Team Name", teamMember);
        entMan.persist(team);
        entMan.flush();

        IsMemberOf isMember = new IsMemberOf(teamMember, team);

        teamMember.getTeams().add(isMember);
        entMan.persist(teamMember);
        entMan.persist(isMember);
        entMan.flush();

        teamMember.getTeams().remove(isMember);
        entMan.remove(teamMember);
        entMan.flush();

        IsMemberOf deletedIsMember = entMan.find(IsMemberOf.class, isMember.getId());
        assertNull(deletedIsMember);
    }

}
