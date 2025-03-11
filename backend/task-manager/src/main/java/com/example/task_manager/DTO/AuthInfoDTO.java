package com.example.task_manager.DTO;

public class AuthInfoDTO {
    private int accountId;
    private String password;
    private String userName;
    private boolean isAdmin;

    public AuthInfoDTO(int accountId, String userName, boolean isAdmin) {
        this.accountId = accountId;
        this.userName = userName;
        this.isAdmin = isAdmin;
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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
