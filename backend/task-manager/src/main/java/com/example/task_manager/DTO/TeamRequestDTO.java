package com.example.task_manager.DTO;

public class TeamRequestDTO {
    private int teamId;
    private String teamName;
    private int teamLeadId;
    
    public TeamRequestDTO() {
    }

    public TeamRequestDTO(int teamId, String teamName, int teamLeadId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLeadId = teamLeadId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(int teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
