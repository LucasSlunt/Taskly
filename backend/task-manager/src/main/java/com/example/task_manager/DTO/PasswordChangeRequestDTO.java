package com.example.task_manager.DTO;

public class PasswordChangeRequestDTO {
    private int teamMemberId;
    private String oldPassword;
    private String newPassword;

    public PasswordChangeRequestDTO() {}

    public PasswordChangeRequestDTO(int teamMemberId, String oldPassword, String newPassword) {
        this.teamMemberId = teamMemberId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(int teamMemberId) {
        this.teamMemberId = teamMemberId;
    }
}
