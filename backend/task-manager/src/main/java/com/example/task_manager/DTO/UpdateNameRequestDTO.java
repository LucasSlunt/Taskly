package com.example.task_manager.DTO;

public class UpdateNameRequestDTO {
    private String newName;

    public UpdateNameRequestDTO() {
    }
    
    public UpdateNameRequestDTO(String newName) {
        this.newName = newName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
