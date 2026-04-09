package com.capstone.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> searchUser(String username) {
        // Intentionally vulnerable
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";

        return jdbcTemplate.queryForList(sql);
    }
}
