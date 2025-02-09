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


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IsAssignedEntityTest {

    @Autowired
    private TestEntityManager entMan;

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

        IsAssigned savedAssignment = entMan.find(IsAssigned.class, isAssigned.getTaskId());
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

        entMan.remove(task);
        entMan.flush();

        IsAssigned deletedAssignment = entMan.find(IsAssigned.class, isAssigned.getTaskId());
        assertNull(deletedAssignment);
    }
}
