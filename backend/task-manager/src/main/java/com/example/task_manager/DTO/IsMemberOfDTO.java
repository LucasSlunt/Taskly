package com.example.task_manager.DTO;

public class IsMemberOfDTO {
    private int isMemberOfId;
    private int teamMemberId;
    private int teamId;

    public IsMemberOfDTO(int isMemberOfId, int teamMemberId, int teamId) {
        this.isMemberOfId = isMemberOfId;
        this.teamMemberId = teamMemberId;
        this.teamId = teamId;
    }

    public int getIsMemberOfId() {
        return isMemberOfId;
    }

    public void setIsMemberOfId(int isMemberOfId) {
        this.isMemberOfId = isMemberOfId;
    }

    public int getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(int teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
