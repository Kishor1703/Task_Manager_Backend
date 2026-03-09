package com.example.TaskManager.Dto;

import java.time.Instant;
import java.time.LocalDate;

import com.example.TaskManager.Model.Task;

public class TaskResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final String priority;
    private final LocalDate dueDate;
    private final String employeeNote;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String assignedEmployeeName;
    private final String assignedEmployeeEmail;
    private final String createdByName;
    private final String createdByEmail;

    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.dueDate = task.getDueDate();
        this.employeeNote = task.getEmployeeNote();
        this.createdAt = task.getCreatedAt();
        this.updatedAt = task.getUpdatedAt();
        this.assignedEmployeeName = task.getAssignedEmployee().getName();
        this.assignedEmployeeEmail = task.getAssignedEmployee().getEmail();
        this.createdByName = task.getCreatedBy().getName();
        this.createdByEmail = task.getCreatedBy().getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getEmployeeNote() {
        return employeeNote;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getAssignedEmployeeName() {
        return assignedEmployeeName;
    }

    public String getAssignedEmployeeEmail() {
        return assignedEmployeeEmail;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }
}
