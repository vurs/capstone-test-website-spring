package com.capstone.services;

import com.capstone.models.AppUser;
import com.capstone.repositories.AppUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;
    private final AppUserRepository appUserRepository;

    public UserService(JdbcTemplate jdbcTemplate, AppUserRepository appUserRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.appUserRepository = appUserRepository;
    }

    public List<Map<String, Object>> searchUser(String username) {
        // Intentionally vulnerable
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";

        return jdbcTemplate.queryForList(sql);
    }

    public Optional<Map<String, Object>> getUserProfile(Long userId) {
        return appUserRepository.findById(userId).map(this::toProfileMap);
    }

    private Map<String, Object> toProfileMap(AppUser user) {
        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("id", user.getId());
        profile.put("username", user.getUsername());
        profile.put("email", user.getEmail());
        profile.put(
                "privateNotes",
                "Confidential internal notes for " + user.getUsername()
                        + " — restricted personnel file, do not share outside HR department."
        );
        return profile;
    }
}
