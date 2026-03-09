package com.example.TaskManager.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.TaskManager.Dto.TaskRequest;
import com.example.TaskManager.Dto.TaskResponse;
import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Model.Role;
import com.example.TaskManager.Model.Task;
import com.example.TaskManager.Repo.TaskRepo.TaskRepo;
import com.example.TaskManager.Repo.UserRepo.UserRepo;

@Service
public class TaskService {
    private static final String DEFAULT_STATUS = "pending";

    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    public TaskService(TaskRepo taskRepo, UserRepo userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public List<TaskResponse> getTasksForUser(String email) {
        AppUser user = getUserByEmail(email);
        List<Task> tasks = user.getRole() == Role.MANAGER
            ? taskRepo.findByCreatedByEmailOrderByIdDesc(email)
            : taskRepo.findByAssignedEmployeeEmailOrderByIdDesc(email);

        return tasks.stream().map(TaskResponse::new).toList();
    }

    public TaskResponse getTaskById(Long id, String email) {
        Task task = getTaskOrThrow(id);
        AppUser user = getUserByEmail(email);

        boolean allowed = user.getRole() == Role.MANAGER
            ? task.getCreatedBy().getEmail().equals(email)
            : task.getAssignedEmployee().getEmail().equals(email);

        if (!allowed) {
            throw new ResponseStatusException(FORBIDDEN, "You do not have access to this task");
        }

        return new TaskResponse(task);
    }

    public TaskResponse createTask(TaskRequest request, String managerEmail) {
        AppUser manager = getUserByEmail(managerEmail);
        if (manager.getRole() != Role.MANAGER) {
            throw new ResponseStatusException(FORBIDDEN, "Only managers can assign tasks");
        }

        if (request.getTitle() == null || request.getTitle().isBlank() || request.getAssignedEmployeeEmail() == null || request.getAssignedEmployeeEmail().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Title and assigned employee email are required");
        }

        AppUser employee = userRepo.findByEmail(request.getAssignedEmployeeEmail().trim().toLowerCase())
            .filter(user -> user.getRole() == Role.EMPLOYEE)
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Assigned employee not found"));

        Task task = new Task();
        task.setTitle(request.getTitle().trim());
        task.setDescription(normalizeText(request.getDescription()));
        task.setStatus(DEFAULT_STATUS);
        task.setPriority(normalizePriority(request.getPriority()));
        task.setDueDate(request.getDueDate());
        task.setEmployeeNote(null);
        task.setCreatedBy(manager);
        task.setAssignedEmployee(employee);

        return new TaskResponse(taskRepo.save(task));
    }

    public TaskResponse updateTaskStatus(Long id, String status, String employeeNote, String employeeEmail) {
        AppUser employee = getUserByEmail(employeeEmail);
        if (employee.getRole() != Role.EMPLOYEE) {
            throw new ResponseStatusException(FORBIDDEN, "Only employees can update task status");
        }

        Task task = getTaskOrThrow(id);
        if (!task.getAssignedEmployee().getEmail().equals(employeeEmail)) {
            throw new ResponseStatusException(FORBIDDEN, "You can only update your assigned tasks");
        }

        task.setStatus(normalizeStatus(status));
        task.setEmployeeNote(normalizeText(employeeNote));
        return new TaskResponse(taskRepo.save(task));
    }

    public void deleteTask(Long id, String managerEmail) {
        AppUser manager = getUserByEmail(managerEmail);
        if (manager.getRole() != Role.MANAGER) {
            throw new ResponseStatusException(FORBIDDEN, "Only managers can delete tasks");
        }

        Task task = getTaskOrThrow(id);
        if (!task.getCreatedBy().getEmail().equals(managerEmail)) {
            throw new ResponseStatusException(FORBIDDEN, "You can only delete tasks you assigned");
        }

        taskRepo.delete(task);
    }

    private Task getTaskOrThrow(Long id) {
        return taskRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found"));
    }

    private AppUser getUserByEmail(String email) {
        return userRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return DEFAULT_STATUS;
        }

        String normalized = status.trim().toLowerCase();
        if (!List.of("pending", "in-progress", "done").contains(normalized)) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid status");
        }
        return normalized;
    }

    private String normalizePriority(String priority) {
        if (priority == null || priority.isBlank()) {
            return "medium";
        }

        String normalized = priority.trim().toLowerCase(Locale.ROOT);
        if (!List.of("low", "medium", "high").contains(normalized)) {
            throw new ResponseStatusException(BAD_REQUEST, "Invalid priority");
        }
        return normalized;
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
