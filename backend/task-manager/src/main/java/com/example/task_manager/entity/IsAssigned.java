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
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
