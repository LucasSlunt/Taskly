package com.example.task_manager.test_helpers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.task_manager.entity.Admin;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.Team;
import com.example.task_manager.entity.TeamMember;
import com.example.task_manager.enums.TaskPriority;

public class EntityTestHelper {
    
    @Autowired
    protected TestEntityManager entityManager;

    protected Admin createUniqueAdmin() {
        return new Admin("Admin_" + System.nanoTime(), "admin_" + System.nanoTime() + "@example.com", "defaultpw");
    }

    protected TeamMember createUniqueTeamMember() {
        return new TeamMember("TeamUser_" + System.nanoTime(), "team_user_" + System.nanoTime() + "@example.com", "defaultpw");
    }

    protected Team createUniqueTeam(TeamMember teamLead) {
        return new Team("Team_" + System.nanoTime(), teamLead);
    }

    protected Team createUniqueTeam() {
        return new Team("Team_" + System.nanoTime(), null);
    }

    protected Task createUniqueTask(Team team) {
        return new Task("Task_" + System.nanoTime(), "Description", team, false, "Open", LocalDate.now(), TaskPriority.LOW);
    }

    

    protected Team createAndPersistTeam() {
        Team team = new Team("Team_" + System.nanoTime(), null);
        entityManager.persist(team);
        entityManager.flush();
        return entityManager.find(Team.class, team.getTeamId());
    }

    protected TeamMember createAndPersistTeamMember() {
        TeamMember teamMember = new TeamMember("User_" + System.nanoTime(), "user_" + System.nanoTime() + "@example.com", "defaultpw");
        entityManager.persist(teamMember);
        entityManager.flush();
        return entityManager.find(TeamMember.class, teamMember.getAccountId());
    }

    protected Task createAndPersistTask(Team team) {
        Task task = new Task("Task_" + System.nanoTime(), "Task description", team, false, "Open", LocalDate.now(), TaskPriority.HIGH);
        entityManager.persist(task);
        entityManager.flush();
        return entityManager.find(Task.class, task.getTaskId());
    }
}
