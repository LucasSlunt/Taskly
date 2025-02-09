package com.example.task_manager.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private boolean isLocked;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate dateCreated;

    private LocalDate expectedCompletionDate;
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "teamId", nullable = true)
    private Team team;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IsAssigned> assignedMembers = new HashSet<>();

    public Task() {}

    public Task(String title, String description, Team team, boolean isLocked, String status, LocalDate dateCreated) {
        this.title = title;
        this.description = description;
        this.team = team;
        this.isLocked = isLocked;
        this.status = status;
        this.dateCreated = dateCreated;
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(LocalDate expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<IsAssigned> getAssignedMembers() {
        return assignedMembers;
    }

    public void setAssignedMembers(Set<IsAssigned> assignedMembers) {
        this.assignedMembers = assignedMembers;
    }
}
