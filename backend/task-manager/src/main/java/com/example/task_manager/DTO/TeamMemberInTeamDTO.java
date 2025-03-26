package com.example.task_manager.DTO;

import com.example.task_manager.enums.RoleType;

public class TeamMemberInTeamDTO {
    private int accountId;
    private String userName;
    private String userEmail;
    private RoleType role;
    private boolean isTeamLead;

    public TeamMemberInTeamDTO(int accountId, String userName, String userEmail, RoleType role, boolean isTeamLead) {
        this.accountId = accountId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
        this.isTeamLead = isTeamLead;
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

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public boolean isIsTeamLead() {
        return isTeamLead;
    }

    public void setIsTeamLead(boolean isTeamLead) {
        this.isTeamLead = isTeamLead;
    }
}
