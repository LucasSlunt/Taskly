package com.example.task_manager.entity;

import jakarta.persistence.*;


@Entity
public class AuthInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        /*
         * To be used when creating a new instance of authinfo, for a team member 
         * or admin whose authinfo already exists
         */
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.teamMember = teamMember;
    }

    //getters and setters
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty.");
        }
        this.hashedPassword = hashedPassword;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        // if (teamMember == null) {
        //     throw new IllegalArgumentException("TeamMember cannot be null.");
        // }
        this.teamMember = teamMember;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        if (salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty.");
        }
        this.salt = salt;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        if (accountId < 0) {
            throw new IllegalArgumentException("Account ID cannot be negative.");
        }
        this.accountId = accountId;
    }

}
