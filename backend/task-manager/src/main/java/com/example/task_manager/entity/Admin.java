package com.example.task_manager.entity;

import com.example.task_manager.enums.RoleType;

import jakarta.persistence.*;

@Entity
public class Admin extends TeamMember {

    public Admin() {}

    public Admin(String userName, String userEmail, String userPassword) {
        super(userName, userEmail, userPassword);
        this.setRole(RoleType.ADMIN);
    }
    //inherits all attributes from TeamMember
}
