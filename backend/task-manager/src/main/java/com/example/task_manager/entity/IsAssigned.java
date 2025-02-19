package com.example.task_manager.entity;

import jakarta.persistence.*;

@Entity
public class IsAssigned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "taskId", nullable = true)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private TeamMember teamMember;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private Team team;

    public IsAssigned() {}

    public IsAssigned(Task task, TeamMember teamMember, Team team) {
        this.task = task;
        this.teamMember = teamMember;
        this.team = team;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be less than 0.");
        }
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        this.task = task;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        if (teamMember == null) {
            throw new IllegalArgumentException("TeamMember cannot be null.");
        }
        this.teamMember = teamMember;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null.");
        }
        this.team = team;
    }
}
