package com.example.TaskManager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TaskManager.Dto.EmployeeSummaryResponse;
import com.example.TaskManager.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/employees")
    public List<EmployeeSummaryResponse> getEmployees(Principal principal) {
        return userService.getEmployeesForManager(principal.getName());
    }
}
