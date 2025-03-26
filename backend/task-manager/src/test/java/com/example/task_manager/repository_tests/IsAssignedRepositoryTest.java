package com.example.task_manager.repository_tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.TaskPriority;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.TaskRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class IsAssignedRepositoryTest{

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IsAssignedRepository isAssignedRepository;

    @Autowired
    private TaskRepository taskRepository;


    /**
     * Creates and persists a unique Team.
     */
    private Team createAndPersistUniqueTeam() {
        Team team = new Team();
        team.setTeamName("Test Team " + System.nanoTime());
        entityManager.persist(team);
        entityManager.flush();
        return team;
    }

    /**
     * Creates and persists a unique TeamMember.
     */
    private TeamMember createAndPersistUniqueTeamMember() {
        TeamMember teamMember = new TeamMember("TestUser" + System.nanoTime(),
                "test" + System.nanoTime() + "@example.com", "defaultpw");
        entityManager.persist(teamMember);
        entityManager.flush();
        return teamMember;
    }

    /**
     * Creates and saves a new Task with a unique title.
     */
    private Task createAndSaveUniqueTask(Team team) {
        Task task = new Task();
        task.setTitle("Implement Feature X " + System.nanoTime());
        task.setStatus("Open");
        task.setTeam(team);
        task.setDateCreated(LocalDate.now());
        task.setPriority(TaskPriority.MEDIUM);
        return taskRepository.save(task);
    }

    /**
     * Creates and saves a new IsAssigned entry with a unique identifier.
     */
    private IsAssigned createAndSaveUniqueAssignment(Task task, TeamMember teamMember, Team team) {
        IsAssigned assignment = new IsAssigned(task, teamMember, team);
        return isAssignedRepository.save(assignment);
    }

    @Test
    void testSaveAssignment() {
        Team team = createAndPersistUniqueTeam();
        Task task = createAndSaveUniqueTask(team);
        TeamMember teamMember = createAndPersistUniqueTeamMember();
        IsAssigned assignment = createAndSaveUniqueAssignment(task, teamMember, team);

        assertNotNull(assignment);
        assertEquals(task.getTaskId(), assignment.getTask().getTaskId());
        assertEquals(teamMember.getAccountId(), assignment.getTeamMember().getAccountId());
    }

    @Test
    void testFindByTeamMemberAndTask() {
        Team team = createAndPersistUniqueTeam();
        TeamMember teamMember = createAndPersistUniqueTeamMember();
        Task task = createAndSaveUniqueTask(team);
        IsAssigned assignment = createAndSaveUniqueAssignment(task, teamMember, team);


        Optional<IsAssigned> foundAssignment = isAssignedRepository.findByTeamMemberAndTask(teamMember, task);
        assertTrue(foundAssignment.isPresent());
        assertEquals(assignment.getId(), foundAssignment.get().getId());
    }

    @Test
    void testExistsByTeamMemberAndTask() {
        Team team = createAndPersistUniqueTeam();
        TeamMember teamMember = createAndPersistUniqueTeamMember();
        Task task = createAndSaveUniqueTask(team);
        IsAssigned assignment = createAndSaveUniqueAssignment(task, teamMember, team);


        boolean exists = isAssignedRepository.existsByTeamMember_AccountIdAndTask_TaskId(teamMember.getAccountId(), task.getTaskId());
        assertTrue(exists);
    }

    @Test
    void testFindAssignmentsByTeamMember() {
        Team team = createAndPersistUniqueTeam();
        Task task = createAndSaveUniqueTask(team);
        TeamMember teamMember = createAndPersistUniqueTeamMember();
        IsAssigned assignment = createAndSaveUniqueAssignment(task, teamMember, team);


        Collection<IsAssigned> assignments = isAssignedRepository.findByTeamMember_AccountId(teamMember.getAccountId());
        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        assertEquals(task.getTaskId(), assignments.iterator().next().getTask().getTaskId());
    }

    @Test
    void testDeleteAssignment() {
        Team team = createAndPersistUniqueTeam();
        TeamMember teamMember = createAndPersistUniqueTeamMember();
        Task task = createAndSaveUniqueTask(team);
        IsAssigned assignment = createAndSaveUniqueAssignment(task, teamMember, team);

        isAssignedRepository.delete(assignment);
        boolean exists = isAssignedRepository.existsByTeamMember_AccountIdAndTask_TaskId(teamMember.getAccountId(), task.getTaskId());
        assertFalse(exists);
    }

    @Test
    void testFindNonExistentAssignment() {
        TeamMember fakeTeamMember = new TeamMember();
        fakeTeamMember.setAccountId(9999);

        Task fakeTask = new Task();
        fakeTask.setTaskId(9999);

        Optional<IsAssigned> foundAssignment = isAssignedRepository.findByTeamMemberAndTask(fakeTeamMember, fakeTask);
        assertFalse(foundAssignment.isPresent());
    }

    @Test
    void testExistsByNonExistentAssignment() {
        boolean exists = isAssignedRepository.existsByTeamMember_AccountIdAndTask_TaskId(9999, 9999);
        assertFalse(exists);
    }

    @Test
    void testFindAssignmentsByTask() {
        Team team = createAndPersistUniqueTeam();
        Task task = createAndSaveUniqueTask(team);

        TeamMember teamMember1 = createAndPersistUniqueTeamMember();
        TeamMember teamMember2 = createAndPersistUniqueTeamMember();

        IsAssigned assignment1 = new IsAssigned(task, teamMember1, team);
        isAssignedRepository.save(assignment1);

        IsAssigned assignment2 = new IsAssigned(task, teamMember2, team);
        isAssignedRepository.save(assignment2);

        Collection<IsAssigned> assignmentsForTask = isAssignedRepository.findByTask(task);

        assertNotNull(assignmentsForTask);
        assertEquals(2, assignmentsForTask.size()); // Since two team members were assigned to the task
        assertTrue(assignmentsForTask.stream().anyMatch(a -> a.getTeamMember().getAccountId() == teamMember1.getAccountId()));
        assertTrue(assignmentsForTask.stream().anyMatch(a -> a.getTeamMember().getAccountId() == teamMember2.getAccountId()));
    }
}
