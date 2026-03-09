package com.example.TaskManager.Dto;

import com.example.TaskManager.Model.AppUser;

public class EmployeeSummaryResponse {
    private final Long id;
    private final String name;
    private final String email;

    public EmployeeSummaryResponse(AppUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
