package com.example.task_manager.entity;

import jakarta.persistence.*;


@Entity
public class AuthInfo {
    @Id
    private int accountId;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String salt;

    @OneToOne
    @MapsId //links AuthInfo to TeamMember using the same primary key (accountId)
    @JoinColumn(name = "accountId") //specifies the foreign key column in AuthInfo
    private TeamMember teamMember;

    public AuthInfo() {}
    
    public AuthInfo(String hashedPassword, String salt, TeamMember teamMember) {
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.teamMember = teamMember;
    }

    //getters and setters
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

}
