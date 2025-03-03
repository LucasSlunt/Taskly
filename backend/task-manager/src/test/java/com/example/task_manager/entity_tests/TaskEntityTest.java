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
public class TaskEntityTest {

    @Autowired
    private TestEntityManager entMan;

    /**
     * Tests if a Task entity can be persisted and retrieved correctly.
     * Ensures that the saved task retains the correct title and is associated with the correct team.
     */
    @Test
    void testTaskPersistence() {
        Team team = new Team();
        team.setTeamName("Test Team Name");
        entMan.persist(team);
        entMan.flush();

        Task task = new Task("Test Task Title", "", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();

        Task savedTask = entMan.find(Task.class, task.getTaskId());
        assertNotNull(savedTask);
        assertEquals("Test Task Title", savedTask.getTitle());
        assertEquals("Test Team Name", savedTask.getTeam().getTeamName());
    }

    /**
     * Tests whether a Task entity correctly tracks assigned team members.
     * Ensures that when a task has an assigned member, it is properly retrieved.
     */
    @Test
    void testTaskWithAssignment() {
        Team team = new Team();
        team.setTeamName("Test Team Name" + System.nanoTime());
        entMan.persist(team);
        entMan.flush();

        Task task = new Task("Test Task Title" + System.nanoTime(), "", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();

        TeamMember teamMember = new TeamMember("Team Member" + System.nanoTime(), System.nanoTime() + "test@example.com","defaultpw");
        entMan.persist(teamMember);
        entMan.flush();

        IsAssigned isAssigned = new IsAssigned();
        isAssigned.setTask(task);
        isAssigned.setTeamMember(teamMember);
        isAssigned.setTeam(team);
        entMan.persist(isAssigned);
        entMan.flush();

        task.getAssignedMembers().add(isAssigned);

        Task savedTask = entMan.find(Task.class, task.getTaskId());
        savedTask.getAssignedMembers().size();

        assertNotNull(savedTask);
        assertEquals(1, savedTask.getAssignedMembers().size());
    }

    /**
     * Tests whether an IsAssigned entity is deleted when the associated Task is removed.
     * Ensures cascading delete behavior works as expected and prevents orphaned assignments.
     */
    @Test
    void testCascadeDeleteWithTask() {
        Team team = new Team();
        team.setTeamName("Test Team Name" + System.nanoTime());
        entMan.persist(team);
        entMan.flush();

        Task task = new Task("Test Task Title" + System.nanoTime(), "", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();
    
        TeamMember teamMember = new TeamMember("Team Member" + System.nanoTime(), System.nanoTime() + "test@example.com","defaultpw");
        entMan.persist(teamMember);
        entMan.flush();

        IsAssigned isAssigned = new IsAssigned(task, teamMember, team);

        task.getAssignedMembers().add(isAssigned);
        
        entMan.persist(isAssigned);
        entMan.flush();
    
        entMan.remove(task);
        entMan.flush();

        IsAssigned deletedIsAssigned = entMan.find(IsAssigned.class, isAssigned.getId());
        assertNull(deletedIsAssigned);
    }
    /*
     * Testing that changing the task status works properly
     */
    @Test
    void testUpdateTaskStatus() {
        Team team = new Team("Status Update Team", null);
        entMan.persist(team);
        entMan.flush();

        Task task = new Task("Task to Update", "Description", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();

        task.setStatus("Completed");
        entMan.persist(task);
        entMan.flush();

        Task updatedTask = entMan.find(Task.class, task.getTaskId());

        assertNotNull(updatedTask);
        assertEquals("Completed", updatedTask.getStatus());
    }

    /*
     * IsLocked should be able to be changed without affecting anything else
     */
    @Test
    void testToggleTaskLock() {
        Team team = new Team("Lock Test Team", null);
        entMan.persist(team);
        entMan.flush();

        Task task = new Task("Task Lock Toggle", "Description", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();

        task.setIsLocked(true);
        entMan.persist(task);
        entMan.flush();

        Task lockedTask = entMan.find(Task.class, task.getTaskId());

        assertNotNull(lockedTask);
        assertTrue(lockedTask.isLocked());

        lockedTask.setIsLocked(false);
        entMan.persist(lockedTask);
        entMan.flush();

        Task unlockedTask = entMan.find(Task.class, task.getTaskId());

        assertNotNull(unlockedTask);
        assertFalse(unlockedTask.isLocked());
    }

    /*
     * Changing task title works properly, and nothing else changes
     */
    @Test
    void testUpdateTaskTitle() {
        Team team = new Team("Title Update Team", null);
        entMan.persist(team);
        entMan.flush();

        Task task = new Task("Old Title", "Description", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();

        task.setTitle("New Task Title");
        entMan.persist(task);
        entMan.flush();

        Task updatedTask = entMan.find(Task.class, task.getTaskId());

        assertNotNull(updatedTask);
        assertEquals("New Task Title", updatedTask.getTitle());
    }

    /*
     * Deleting a Task does not delete a Team or TeamMember
     */
    @Test
    void testDeleteTaskDoesNotDeleteTeamOrTeamMember() {
        Team team = new Team("Independent Team", null);
        entMan.persist(team);
        entMan.flush();

        TeamMember teamMember = new TeamMember("Task Member", "member@example.com","defaultpw");
        entMan.persist(teamMember);
        entMan.flush();

        Task task = new Task("Standalone Task", "Task that should remain", team, false, "Open", LocalDate.now());
        entMan.persist(task);
        entMan.flush();

        IsAssigned isAssigned = new IsAssigned(task, teamMember, team);
        entMan.persist(isAssigned);
        entMan.flush();

        entMan.remove(task);

        assertNotNull(entMan.find(Team.class, team.getTeamId()));
        assertNotNull(entMan.find(TeamMember.class, teamMember.getAccountId()));
    }

}
