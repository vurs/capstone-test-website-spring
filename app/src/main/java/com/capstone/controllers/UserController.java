package com.capstone.controllers;

import com.capstone.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String username) {
        return userService.searchUser(username);
    }

    @GetMapping("/profile")
    @Operation(
            summary = "Get user profile by ID",
            description = "Intentionally vulnerable endpoint for broken access control (IDOR) scanner testing."
    )
    public Map<String, Object> getProfile(
            @Parameter(name = "userId", description = "Numeric user identifier", example = "1")
            @RequestParam Long userId) {
        return userService.getUserProfile(userId)
                .orElseGet(() -> Map.of(
                        "error", "User not found",
                        "userId", userId,
                        "message", "No user exists with the requested identifier."
                ));
    }

}