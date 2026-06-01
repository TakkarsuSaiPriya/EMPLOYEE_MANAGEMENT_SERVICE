package com.cts.employee.controller;

import com.cts.employee.entity.User;
import com.cts.employee.exception.BadRequestException;
import com.cts.employee.repository.UserRepository;
import com.cts.employee.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // ✅ REGISTER USER
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        log.info("Registering user: {}", user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }

        // default role
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        userRepository.save(user);

        log.info("User registered successfully: {}", user.getUsername());

        return "User registered successfully";
    }

    // ✅ LOGIN USER
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {

        String username = req.get("username");
        String password = req.get("password");

        log.info("Login attempt for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Invalid username"));

        if (!user.getPassword().equals(password)) {
            log.error("Invalid password for user: {}", username);
            throw new BadRequestException("Invalid password");
        }

        String token = jwtUtil.generateToken(username, user.getRole());

        log.info("Login successful for user: {}", username);

        return Map.of("token", token);
    }
}
