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
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be less than 0.");
        }
        this.id = id;
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

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        if (teamMember == null) {
            throw new IllegalArgumentException("TeamMember cannot be null.");
        }
        this.teamMember = teamMember;
    }
}
