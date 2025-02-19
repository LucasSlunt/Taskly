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
    @JoinColumn(name = "teamLeadId", referencedColumnName = "accountId", nullable = true)
    private TeamMember teamLead;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true) //deleting a team removes all IsMemberOf records
    private Set<IsMemberOf> members = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true) //deleting a team removes all its tasks
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true) //deleting a team removes all IsAssigned records
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
        if (teamId < 0) {
            throw new IllegalArgumentException("Team ID cannot be less than 0.");
        }
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty.");
        }
        this.teamName = teamName;
    }

    public TeamMember getTeamLead() {
        return teamLead;
    }

    //Team lead CAN be empty
    public void setTeamLead(TeamMember teamLead) {
        this.teamLead = teamLead;
    }

    public Set<IsMemberOf> getMembers() {
        return members;
    }

    //members can be empty but not null
    //if members is null, an empty set is initialized
    public void setMembers(Set<IsMemberOf> members) {
        this.members = (members != null) ? members : new HashSet<>();
    }    

    public Set<IsAssigned> getAssignedTasks() {
        return assignedTasks;
    }

    //assignedTasks can be empty but not null
    //if assignedTasks is null, an empty set is initialized
    public void setAssignedTasks(Set<IsAssigned> assignedTasks) {
        this.assignedTasks = (assignedTasks != null) ? assignedTasks : new HashSet<>();
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    //tasks can be empty but not null
    //if tasks is null, an empty set is initialized
    public void setTasks(Set<Task> tasks) {
        this.tasks = (tasks != null) ? tasks : new HashSet<>();
    }
}
