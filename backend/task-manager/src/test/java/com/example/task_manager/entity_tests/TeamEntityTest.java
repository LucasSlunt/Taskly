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
import com.example.task_manager.entity.IsMemberOf;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamEntityTest {

    @Autowired
    private TestEntityManager entMan;

    /**
     * Tests if a Team entity can be persisted and retrieved correctly.
     * Ensures that the saved team retains the correct name and team lead.
     */
    @Test
    void testTeamPersistence() {
        TeamMember teamMember = new TeamMember("Test User", "test@example.com","defaultpw");
        entMan.persist(teamMember);
        entMan.flush();

        Team team = new Team();
        team.setTeamName("Test Team Name");
        team.setTeamLead(teamMember);
        entMan.persist(team);
        entMan.flush();

        Team savedTeam = entMan.find(Team.class, team.getTeamId());
        assertNotNull(savedTeam);
        assertEquals("Test Team Name", savedTeam.getTeamName());
        assertEquals("Test User", savedTeam.getTeamLead().getUserName());
    }

    /**
     * Tests whether a Team entity correctly tracks its associated members.
     * Ensures that when members are added to a team, they are properly retrieved.
     */
    @Test
    void testTeamWithMembers() {
        TeamMember teamMember = new TeamMember("Test User", "test@example.com","defaultpw");
        entMan.persist(teamMember);
        entMan.flush();

        Team team = new Team();
        team.setTeamName("Test Team Name");
        team.setTeamLead(teamMember);
        entMan.persist(team);
        entMan.flush();

        TeamMember member_1 = new TeamMember("John Doe", "john@example.com","defaultpw");
        TeamMember member_2 = new TeamMember("Alice Chains", "alice_chains@example.com","defaultpw");
        entMan.persist(member_1);
        entMan.persist(member_2);
        entMan.flush();

        IsMemberOf isMember_1 = new IsMemberOf();
        isMember_1.setTeam(team);
        isMember_1.setTeamMember(member_1);
        IsMemberOf isMember_2 = new IsMemberOf();
        isMember_2.setTeam(team);
        isMember_2.setTeamMember(member_2);

        team.getMembers().add(isMember_1);
        team.getMembers().add(isMember_2);

        entMan.persist(isMember_1);
        entMan.persist(isMember_2);
        entMan.flush();

        Team savedTeam = entMan.find(Team.class, team.getTeamId());
        savedTeam.getMembers().size();

        System.out.println("Saved Team Members: " + savedTeam.getMembers());

        assertNotNull(savedTeam);
        assertEquals(2, savedTeam.getMembers().size());
    }

    /**
     * Tests whether an IsAssigned entity is deleted when the associated Team is removed.
     * Ensures cascading delete behavior works as expected and prevents orphaned assignments.
     */
    @Test
    void testCascadeDeleteWithTeam() {
        TeamMember teamMember = new TeamMember("Test User", "test@example.com","defaultpw");
        entMan.persist(teamMember);
        entMan.flush();

        Team team = new Team();
        team.setTeamName("Test Team Name");
        team.setTeamLead(teamMember);
        entMan.persist(team);
        entMan.flush();

        entMan.refresh(team);

        Task task = new Task("Test Task Title", "Test description.", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();

        TeamMember devMember = new TeamMember("Backend Member", "backend@example.com","defaultpw");
        entMan.persist(devMember);
        entMan.flush();

        IsAssigned isAssigned = new IsAssigned(task, devMember, team);
        team.getAssignedTasks().add(isAssigned);
        entMan.persist(isAssigned);
        entMan.flush();

        assertNotNull(entMan.find(IsAssigned.class, isAssigned.getId()));
        assertNotNull(entMan.find(Task.class, task.getTaskId()));

        entMan.remove(team);

        IsAssigned deletedAssignment = entMan.find(IsAssigned.class, isAssigned.getId());
        assertNull(deletedAssignment);
    }

    /*
     * Changing teamLead updates reference correctly
     */
    @Test
    void testUpdateTeamLead() {
        TeamMember oldLead = new TeamMember("Old Lead", "oldlead@example.com","defaultpw");
        TeamMember newLead = new TeamMember("New Lead", "newlead@example.com","defaultpw");
        entMan.persist(oldLead);
        entMan.persist(newLead);
        entMan.flush();

        Team team = new Team("Leadership Change Team", oldLead);
        entMan.persist(team);
        entMan.flush();

        team.setTeamLead(newLead);
        entMan.persist(team);
        entMan.flush();

        Team updatedTeam = entMan.find(Team.class, team.getTeamId());

        assertNotNull(updatedTeam);
        assertEquals(newLead.getAccountId(), updatedTeam.getTeamLead().getAccountId());
    }

    /*
     * Test a team with a teamLead set to null
     */
    @Test
    void testTeamWithoutTeamLead() {
        Team team = new Team();
        team.setTeamName("Team Without Lead");
        entMan.persist(team);
        entMan.flush();

        Team savedTeam = entMan.find(Team.class, team.getTeamId());

        assertNotNull(savedTeam);
        assertNull(savedTeam.getTeamLead());
    }

    /*
     * Deleting a team does NOT delete TeamMembers assigned to the team
     */
    @Test
    void testDeleteTeamDoesNotDeleteMembers() {
        TeamMember teamMember1 = new TeamMember("Member1", "member1@example.com","defaultpw");
        TeamMember teamMember2 = new TeamMember("Member2", "member2@example.com","defaultpw");
        entMan.persist(teamMember1);
        entMan.persist(teamMember2);
        entMan.flush();

        Team team = new Team("Team with Members", null);
        entMan.persist(team);
        entMan.flush();

        IsMemberOf isMember1 = new IsMemberOf(teamMember1, team);
        IsMemberOf isMember2 = new IsMemberOf(teamMember2, team);
        entMan.persist(isMember1);
        entMan.persist(isMember2);
        entMan.flush();

        entMan.remove(team);

        assertNotNull(entMan.find(TeamMember.class, teamMember1.getAccountId()));
        assertNotNull(entMan.find(TeamMember.class, teamMember2.getAccountId()));
    }

    /*
     * Ensure team name updates properly
     */
    @Test
    void testUpdateTeamName() {
        Team team = new Team("Old Team Name", null);
        entMan.persist(team);
        entMan.flush();

        team.setTeamName("New Team Name");
        entMan.persist(team);
        entMan.flush();

        Team updatedTeam = entMan.find(Team.class, team.getTeamId());

        assertNotNull(updatedTeam);
        assertEquals("New Team Name", updatedTeam.getTeamName());
    }
}
