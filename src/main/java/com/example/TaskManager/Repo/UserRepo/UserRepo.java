package com.example.TaskManager.Repo.UserRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Model.Role;

public interface UserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    List<AppUser> findByRoleOrderByNameAsc(Role role);
}
