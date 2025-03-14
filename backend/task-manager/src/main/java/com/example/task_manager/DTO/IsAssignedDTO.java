package com.example.task_manager.DTO;

public class IsAssignedDTO {
    private int isAssignedId;    
    private int taskId;
    private int teamMemberId;
    private int teamId;

    public IsAssignedDTO(int isAssignedId, int taskId, int teamMemberId, int teamId) {
        this.isAssignedId = isAssignedId;
        this.taskId = taskId;
        this.teamMemberId = teamMemberId;
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "IsAssignedDTO{" +
                "isAssignedId=" + isAssignedId +
                ", taskId=" + taskId +
                ", teamMemberId=" + teamMemberId +
                ", teamId=" + teamId +
                '}';
    }


    public int getIsAssignedId() {
        return isAssignedId;
    }

    public void setIsAssignedId(int isAssignedId) {
        this.isAssignedId = isAssignedId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
