package com.example.task_manager.entity;

import jakarta.persistence.*;

@Entity
public class Admin extends TeamMember {

    public Admin() {}

    public Admin(String userName, String userEmail, String userPassword) {
        super(userName, userEmail, userPassword);
    }
    //inherits all attributes from TeamMember
}
