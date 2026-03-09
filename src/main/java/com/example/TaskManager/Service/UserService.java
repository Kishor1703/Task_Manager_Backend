package com.example.TaskManager.Service;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.TaskManager.Dto.EmployeeSummaryResponse;
import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Model.Role;
import com.example.TaskManager.Repo.UserRepo.UserRepo;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<EmployeeSummaryResponse> getEmployeesForManager(String email) {
        AppUser user = userRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        if (user.getRole() != Role.MANAGER) {
            throw new ResponseStatusException(FORBIDDEN, "Only managers can view employees");
        }

        return userRepo.findByRoleOrderByNameAsc(Role.EMPLOYEE)
            .stream()
            .map(EmployeeSummaryResponse::new)
            .toList();
    }
}
