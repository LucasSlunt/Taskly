package com.example.task_manager.DTO;

import java.time.LocalDate;

public class TaskDTO {
    private int taskId;
    private String title;
    private String description;
    private boolean isLocked;
    private String status;
    private LocalDate dueDate;
    private int teamId;

    public TaskDTO() {}

    public TaskDTO(int taskId, String title, String description, boolean isLocked, String status, LocalDate dueDate, int teamId) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.isLocked = isLocked;
        this.status = status;
        this.dueDate = dueDate;
        this.teamId = teamId;
    }

    //getters and setters
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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
