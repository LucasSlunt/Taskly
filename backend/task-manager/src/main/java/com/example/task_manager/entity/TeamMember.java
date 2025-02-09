package com.example.task_manager.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @OneToOne(mappedBy = "teamMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthInfo authInfo;

    @OneToMany(mappedBy = "teamMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IsMemberOf> teams = new HashSet<>();

    @OneToMany(mappedBy = "teamMember")
    private Set<IsAssigned> assignedTasks = new HashSet<>();

    public TeamMember() {}

    public TeamMember(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.teams = new HashSet<>();
        this.assignedTasks = new HashSet<>();
    }

    //getters and setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthInfo authInfo) {
        this.authInfo = authInfo;
    }

    public Set<IsMemberOf> getTeams() {
        return teams;
    }

    public void setTeams(Set<IsMemberOf> teams) {
        this.teams = teams;
    }

    public Set<IsAssigned> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Set<IsAssigned> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

}
