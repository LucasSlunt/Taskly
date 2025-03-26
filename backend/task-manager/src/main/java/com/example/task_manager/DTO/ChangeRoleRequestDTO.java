package com.example.task_manager.DTO;

import com.example.task_manager.enums.RoleType;

public class ChangeRoleRequestDTO {
    private RoleType role;

    public ChangeRoleRequestDTO() {
    }

    public ChangeRoleRequestDTO(RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
