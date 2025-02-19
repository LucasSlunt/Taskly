package com.example.task_manager.DTO;

public class AuthInfoDTO {
    private int accountId;
    private int teamMemberId;

    public AuthInfoDTO(int accountId, int teamMemberId) {
        this.accountId = accountId;
        this.teamMemberId = teamMemberId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(int teamMemberId) {
        this.teamMemberId = teamMemberId;
    }
}
