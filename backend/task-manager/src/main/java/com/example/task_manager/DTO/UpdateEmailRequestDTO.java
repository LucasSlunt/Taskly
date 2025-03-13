package com.example.task_manager.DTO;

public class UpdateEmailRequestDTO {
    private String newEmail;

    public UpdateEmailRequestDTO() {
    }
    
    public UpdateEmailRequestDTO(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
