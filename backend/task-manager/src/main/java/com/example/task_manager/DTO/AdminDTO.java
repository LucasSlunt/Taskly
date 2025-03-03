package com.example.task_manager.DTO;

public class AdminDTO {
    private int accountId;
    private String userName;
    private String userEmail;

    public AdminDTO(int accountId, String userName, String userEmail) {
        this.accountId = accountId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    //getters and setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
