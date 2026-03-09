package com.example.TaskManager.Dto;

public class UserProfileResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String role;

    public UserProfileResponse(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
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

    public String getRole() {
        return role;
    }
}
