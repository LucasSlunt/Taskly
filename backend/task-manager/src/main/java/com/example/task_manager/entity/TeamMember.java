package com.example.task_manager.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.task_manager.service.AuthInfoService;

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

    @OneToMany(mappedBy = "teamMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IsAssigned> assignedTasks = new HashSet<>();

    public TeamMember() {}

    public TeamMember(String userName, String userEmail, String rawPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        String userSalt = AuthInfoService.generateSalt();
        this.authInfo = new AuthInfo(AuthInfoService.hashPassword(rawPassword,userSalt), userSalt, this);
        this.teams = new HashSet<>();
        this.assignedTasks = new HashSet<>();
    }

    //getters and setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        if (accountId < 0) {
            throw new IllegalArgumentException("Account ID cannot be less than 0.");
        }
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty.");
        }
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        if (userEmail == null || userEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        this.userEmail = userEmail;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthInfo authInfo) {
        if (authInfo == null) {
            throw new IllegalArgumentException("Auth Info cannot be null.");
        }
        this.authInfo = authInfo;
    }

    public Set<IsMemberOf> getTeams() {
        return teams;
    }

    //teams can be empty but not null
    //if teams is null, an empty set is initialized
    public void setTeams(Set<IsMemberOf> teams) {
        this.teams = (teams != null) ? teams : new HashSet<>();
    }

    public Set<IsAssigned> getAssignedTasks() {
        return assignedTasks;
    }

    //assignedTasks can be empty but not null
    //if assignedTasks is null, an empty set is initialized
    public void setAssignedTasks(Set<IsAssigned> assignedTasks) {
        this.assignedTasks = (assignedTasks != null) ? assignedTasks : new HashSet<>();
    }

}
