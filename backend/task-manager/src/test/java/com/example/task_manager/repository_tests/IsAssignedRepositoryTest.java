package com.example.task_manager.repository_tests;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.test_helpers.RepositoryTestHelper;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class IsAssignedRepositoryTest extends RepositoryTestHelper{

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
        assertTrue(assignmentsForTask.stream()
                .anyMatch(a -> a.getTeamMember().getAccountId() == teamMember1.getAccountId()));
        assertTrue(assignmentsForTask.stream()
                .anyMatch(a -> a.getTeamMember().getAccountId() == teamMember2.getAccountId()));
    }
    
    @Test
    void testDeleteAllAssignmentsByTaskId() {
        Team team = createAndPersistUniqueTeam();
        Task task = createAndSaveUniqueTask(team);

        TeamMember member1 = createAndPersistUniqueTeamMember();
        TeamMember member2 = createAndPersistUniqueTeamMember();

        isAssignedRepository.save(new IsAssigned(task, member1, team));
        isAssignedRepository.save(new IsAssigned(task, member2, team));

        Collection<IsAssigned> assignmentsBefore = isAssignedRepository.findByTask(task);
        assertEquals(2, assignmentsBefore.size());

        isAssignedRepository.deleteAllByTask_TaskId(task.getTaskId());

        Collection<IsAssigned> assignmentsAfter = isAssignedRepository.findByTask(task);
        assertEquals(0, assignmentsAfter.size());
    }

}
