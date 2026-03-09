package com.example.TaskManager.Repo.TaskRepo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.TaskManager.Model.Task;

public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByCreatedByEmailOrderByIdDesc(String email);
    List<Task> findByAssignedEmployeeEmailOrderByIdDesc(String email);
}
