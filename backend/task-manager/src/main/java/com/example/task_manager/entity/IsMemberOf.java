package com.example.task_manager.entity;

import jakarta.persistence.*;

@Entity
public class IsMemberOf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private TeamMember teamMember;

    @ManyToOne
    @JoinColumn(name = "teamId", nullable = true)
    private Team team;

    public IsMemberOf() {}

    public IsMemberOf(TeamMember teamMember, Team team) {
        this.teamMember = teamMember;
        this.team = team;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }
}
