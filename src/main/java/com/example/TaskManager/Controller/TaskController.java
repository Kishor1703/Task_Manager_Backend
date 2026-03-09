package com.example.TaskManager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TaskManager.Dto.TaskRequest;
import com.example.TaskManager.Dto.TaskResponse;
import com.example.TaskManager.Dto.TaskStatusUpdateRequest;
import com.example.TaskManager.Service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getAllTasks(Principal principal) {
        return taskService.getTasksForUser(principal.getName());
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id, Principal principal) {
        return taskService.getTaskById(id, principal.getName());
    }

    @PostMapping
    public TaskResponse createTask(@RequestBody TaskRequest request, Principal principal) {
        return taskService.createTask(request, principal.getName());
    }

    @PutMapping("/{id}/status")
    public TaskResponse updateTaskStatus(
        @PathVariable Long id,
        @RequestBody TaskStatusUpdateRequest request,
        Principal principal
    ) {
        return taskService.updateTaskStatus(id, request.getStatus(), request.getEmployeeNote(), principal.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, Principal principal) {
        taskService.deleteTask(id, principal.getName());
    }
}
