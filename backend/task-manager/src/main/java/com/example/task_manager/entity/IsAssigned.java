package com.example.task_manager.entity;

import jakarta.persistence.*;

@Entity
public class IsAssigned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "taskId", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private TeamMember teamMember;

    @ManyToOne
    @JoinColumn(name = "teamId", nullable = false)
    private Team team;

    public IsAssigned() {}
    
    public IsAssigned(Task task, TeamMember teamMember, Team team) {
        this.task = task;
        this.teamMember = teamMember;
        this.team = team;
    }


    //getters and setters
    public int getTaskId() {
        return id;
    }

    public void setTaskId(int taskId) {
        this.id = taskId;
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

