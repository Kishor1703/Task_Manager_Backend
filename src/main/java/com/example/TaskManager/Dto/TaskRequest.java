package com.example.TaskManager.Dto;

import java.time.LocalDate;

public class TaskRequest {
    private String title;
    private String description;
    private String priority;
    private LocalDate dueDate;
    private String assignedEmployeeEmail;

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getAssignedEmployeeEmail() {
        return assignedEmployeeEmail;
    }

    public void setAssignedEmployeeEmail(String assignedEmployeeEmail) {
        this.assignedEmployeeEmail = assignedEmployeeEmail;
    }
}
