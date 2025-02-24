package com.example.task_manager.repository_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IsAssignedRepositoryTest {
    @Autowired
    private IsAssignedRepository isAssignedRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Task task;
    private TeamMember teamMember;
    private Team team;
    private IsAssigned assignment;

    @BeforeEach
    void setUp() {
        // Clear all previous data
        isAssignedRepository.deleteAllInBatch();
        taskRepository.deleteAllInBatch();
        teamRepository.deleteAllInBatch();
        teamMemberRepository.deleteAllInBatch();

        // Create and save a Team
        team = new Team();
        team.setTeamName("Development Team");
        team = teamRepository.save(team);

        // Create and save a Team Member
        teamMember = new TeamMember("John Doe", "john" + System.nanoTime() + "@example.com");
        teamMember = teamMemberRepository.save(teamMember);

        // Create and save a Task
        task = new Task();
        task.setTitle("Implement Feature X");
        task.setStatus("Open");
        task.setTeam(team);
        task.setDateCreated(LocalDate.now());
        task = taskRepository.save(task);

        // Create and save an assignment
        assignment = new IsAssigned(task, teamMember, team);
        assignment = isAssignedRepository.save(assignment);
    }

    @Test
    void testSaveAssignment() {
        assertNotNull(assignment);
        assertEquals(task.getTaskId(), assignment.getTask().getTaskId());
        assertEquals(teamMember.getAccountId(), assignment.getTeamMember().getAccountId());
    }

    @Test
    void testFindByTeamMemberAndTask() {
        Optional<IsAssigned> foundAssignment = isAssignedRepository.findByTeamMemberAndTask(teamMember, task);
        assertTrue(foundAssignment.isPresent());
        assertEquals(assignment.getId(), foundAssignment.get().getId());
    }

    @Test
    void testExistsByTeamMemberAndTask() {
        boolean exists = isAssignedRepository.existsByTeamMember_AccountIdAndTask_TaskId(teamMember.getAccountId(), task.getTaskId());
        assertTrue(exists);
    }

    @Test
    void testFindAssignmentsByTeamMember() {
        Collection<IsAssigned> assignments = isAssignedRepository.findByTeamMember_AccountId(teamMember.getAccountId());
        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        assertEquals(task.getTaskId(), assignments.iterator().next().getTask().getTaskId());
    }

    @Test
    void testDeleteAssignment() {
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
}
