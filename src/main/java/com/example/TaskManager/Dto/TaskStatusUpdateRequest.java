package com.example.TaskManager.Dto;

public class TaskStatusUpdateRequest {
    private String status;
    private String employeeNote;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeNote() {
        return employeeNote;
    }

    public void setEmployeeNote(String employeeNote) {
        this.employeeNote = employeeNote;
    }
}
