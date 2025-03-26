package com.example.task_manager.repository_tests;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.IsAssigned;
import com.example.task_manager.entity.IsMemberOf;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.TaskPriority;
import com.example.task_manager.repository.AuthInfoRepository;
import com.example.task_manager.repository.IsAssignedRepository;
import com.example.task_manager.repository.IsMemberOfRepository;
import com.example.task_manager.repository.NotificationRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.TeamMemberRepository;
import com.example.task_manager.repository.TeamRepository;

public class RepositoryTestHelper {

    @Autowired
    protected TestEntityManager entityManager;

    @Autowired
    protected IsAssignedRepository isAssignedRepository;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected IsMemberOfRepository isMemberOfRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected TeamMemberRepository teamMemberRepository;

    @Autowired
    protected AuthInfoRepository authInfoRepository;

    @Autowired
    protected NotificationRepository notificationRepository;


    /**
     * Creates and persists a unique Team.
     */
    protected Team createAndPersistUniqueTeam() {
        Team team = new Team();
        team.setTeamName("Test Team " + System.nanoTime());
        entityManager.persist(team);
        entityManager.flush();
        return team;
    }

    /**
     * Creates and persists a unique TeamMember.
     */
    protected TeamMember createAndPersistUniqueTeamMember() {
        TeamMember teamMember = new TeamMember("TestUser" + System.nanoTime(),
                "test" + System.nanoTime() + "@example.com", "defaultpw");
        entityManager.persist(teamMember);
        entityManager.flush();
        return teamMember;
    }

        protected IsMemberOf createAndPersistUniqueMembership(Team team, TeamMember teamMember) {
        IsMemberOf isMemberOf = new IsMemberOf();
        isMemberOf.setTeam(team);
        isMemberOf.setTeamMember(teamMember);
        entityManager.persist(isMemberOf);
        entityManager.flush();
        return isMemberOf;
    }

    /**
     * Creates and saves a new Task with a unique title.
     */
    protected Task createAndSaveUniqueTask(Team team) {
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
    protected IsAssigned createAndSaveUniqueAssignment(Task task, TeamMember teamMember, Team team) {
        IsAssigned assignment = new IsAssigned(task, teamMember, team);
        return isAssignedRepository.save(assignment);
    }
}
