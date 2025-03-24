package com.example.task_manager.DTO;

import java.util.List;

import com.example.task_manager.enums.RoleType;

public class TeamMemberWithTeamLeadDTO {
    private int accountId;
    private String userName;
    private String userEmail;
    private RoleType role;
    private boolean isTeamLead;
    private List<Integer> teamLeadOfId;
    private List<String> teamLeadOfName;

    public TeamMemberWithTeamLeadDTO(int accountId, String userName, String userEmail, RoleType role, boolean isTeamLead, List<Integer> teamLeadOfId, List<String> teamLeadOfName) {
        this.accountId = accountId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
        this.isTeamLead = isTeamLead;
        this.teamLeadOfId = teamLeadOfId;
        this.teamLeadOfName = teamLeadOfName;
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

    public List<Integer> getTeamLeadOfId() {
        return teamLeadOfId;
    }

    public void setTeamLeadOfId(List<Integer> teamLeadOfId) {
        this.teamLeadOfId = teamLeadOfId;
    }

    public List<String> getTeamLeadOfName() {
        return teamLeadOfName;
    }

    public void setTeamLeadOfName(List<String> teamLeadOfName) {
        this.teamLeadOfName = teamLeadOfName;
    }
}
