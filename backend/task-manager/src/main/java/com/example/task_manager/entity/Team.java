package com.example.task_manager.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    @Column(nullable = false, unique = true)
    private String teamName;

    @ManyToOne
    @JoinColumn(name = "teamLeadId", referencedColumnName = "accountId")
    private TeamMember teamLead;

    @OneToMany(mappedBy = "team")
    private Set<IsMemberOf> members = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true) //When Team is deleted, IsAssigned is also deleted
    private Set<IsAssigned> assignedTasks = new HashSet<>();

    public Team() {}

    public Team(String teamName, TeamMember teamLead) {
        this.teamName = teamName;
        this.teamLead = teamLead;
    }

    //getters and setters

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public TeamMember getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(TeamMember teamLead) {
        this.teamLead = teamLead;
    }

    public Set<IsMemberOf> getMembers() {
        return members;
    }

    public void setMembers(Set<IsMemberOf> members) {
        this.members = members;
    }

    public Set<IsAssigned> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Set<IsAssigned> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
}
