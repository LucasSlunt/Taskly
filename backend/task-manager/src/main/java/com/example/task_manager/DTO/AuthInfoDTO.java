package com.example.task_manager.DTO;

import com.example.task_manager.enums.RoleType;

public class AuthInfoDTO {
    private int accountId;
    private String password;
    private String userName;
    private RoleType role;

    public AuthInfoDTO(int accountId, String userName, RoleType role) {
        this.accountId = accountId;
        this.userName = userName;
        this.role = role;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
