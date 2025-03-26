package com.example.task_manager.entity_tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.test_helpers.EntityTestHelper;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class IsMemberOfEntityTest extends EntityTestHelper{


    @Test
    void testIsMemberOfPersistence() {
        TeamMember teamMember = createUniqueTeamMember();
        entityManager.persist(teamMember);
        entityManager.flush();

        Team team = createUniqueTeam(teamMember);
        entityManager.persist(team);
        entityManager.flush();

        IsMemberOf isMember = new IsMemberOf(teamMember, team);
        entityManager.persist(isMember);
        entityManager.flush();

        IsMemberOf savedIsMember = entityManager.find(IsMemberOf.class, isMember.getId());

        assertNotNull(savedIsMember);
        assertEquals(teamMember.getUserName(), savedIsMember.getTeamMember().getUserName());
        assertEquals(team.getTeamName(), savedIsMember.getTeam().getTeamName());
    }

    @Test
    void testIsMemberOfFailsWithoutTeamMember() {
        Team team = createUniqueTeam(null);
        entityManager.persist(team);
        entityManager.flush();

        IsMemberOf isMember = new IsMemberOf(null, team);

        Exception e = assertThrows(Exception.class, () -> {
            entityManager.persist(isMember);
            entityManager.flush();
        });

        assertNotNull(e);
    }

    @Test
    void testCascadeDeleteIsMember() {
        TeamMember teamMember = createUniqueTeamMember();
        entityManager.persist(teamMember);
        entityManager.flush();

        Team team = createUniqueTeam(teamMember);
        entityManager.persist(team);
        entityManager.flush();

        IsMemberOf isMember = new IsMemberOf(teamMember, team);
        teamMember.getTeams().add(isMember);

        entityManager.persist(teamMember);
        entityManager.persist(isMember);
        entityManager.flush();

        teamMember.getTeams().remove(isMember);
        entityManager.remove(isMember.getTeam());
        entityManager.flush();

        assertNull(entityManager.find(IsMemberOf.class, isMember.getId()));
    }

    @Test
    void testCascadeDeleteIsMemberWithTeam() {
        TeamMember teamMember = createUniqueTeamMember();
        entityManager.persist(teamMember);
        entityManager.flush();

        Team team = createUniqueTeam(teamMember);
        entityManager.persist(team);
        entityManager.flush();

        IsMemberOf isMember = new IsMemberOf(teamMember, team);
        team.getMembers().add(isMember);

        entityManager.persist(isMember);
        entityManager.flush();

        entityManager.remove(team);
        entityManager.flush();

        assertNull(entityManager.find(IsMemberOf.class, isMember.getId()));
    }

    @Test
    void testRemoveIsMemberOfWithoutAffectingEntities() {
        TeamMember teamMember = createUniqueTeamMember();
        entityManager.persist(teamMember);
        entityManager.flush();

        Team team = createUniqueTeam(teamMember);
        entityManager.persist(team);
        entityManager.flush();

        IsMemberOf isMember = new IsMemberOf(teamMember, team);
        entityManager.persist(isMember);
        entityManager.flush();

        entityManager.remove(isMember);
        entityManager.flush();

        assertNotNull(entityManager.find(TeamMember.class, teamMember.getAccountId()));
        assertNotNull(entityManager.find(Team.class, team.getTeamId()));
    }
}
