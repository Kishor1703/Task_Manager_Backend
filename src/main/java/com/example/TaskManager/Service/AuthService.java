package com.example.TaskManager.Service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.TaskManager.Dto.AuthRequest;
import com.example.TaskManager.Dto.AuthResponse;
import com.example.TaskManager.Dto.RegisterRequest;
import com.example.TaskManager.Dto.UserProfileResponse;
import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Model.Role;
import com.example.TaskManager.Repo.UserRepo.UserRepo;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
        UserRepo userRepo,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null || request.getName() == null || request.getRole() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Name, email, password, and role are required");
        }

        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepo.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(BAD_REQUEST, "Email is already registered");
        }

        AppUser user = new AppUser();
        user.setName(request.getName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(parseRole(request.getRole()));

        AppUser savedUser = userRepo.save(user);
        String token = jwtService.generateToken(savedUser);
        return new AuthResponse(token, savedUser.getName(), savedUser.getEmail(), savedUser.getRole().name());
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AppUser user = (AppUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }

    public UserProfileResponse getCurrentUser(String email) {
        AppUser user = userRepo.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "User not found"));

        return new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    private Role parseRole(String role) {
        try {
            return Role.valueOf(role.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(BAD_REQUEST, "Role must be MANAGER or EMPLOYEE");
        }
    }
}
