package com.capstone.security;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/api/auth/login",
                        "/users/**",
                        "/network/**",
                        "/unsafe-forms/**"
                ))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/csrf-demo", "/csrf-demo/**").permitAll()
                        .requestMatchers("/clickjacking-demo", "/clickjacking-demo/**").permitAll()
                        .requestMatchers("/unsafe-forms/**").permitAll()
                        .requestMatchers(
                                "/admin", "/admin/",
                                "/backup", "/backup/",
                                "/uploads", "/uploads/",
                                "/config", "/config/",
                                "/debug-info",
                                "/.env",
                                "/config.yml",
                                "/backup.zip",
                                "/backup.tar.gz",
                                "/db_backup.sql",
                                "/database.sql",
                                "/site.bak",
                                "/app.bak",
                                "/.git/config"
                        ).permitAll()
                        // OpenAPI spec and scanner-friendly REST endpoints (no Keycloak required).
                        .requestMatchers(
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/auth/login",
                                // Permit so the controller can return 401 JSON instead of a Keycloak redirect.
                                "/api/session/me",
                                "/users/**",
                                "/network/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // Intentionally disable Spring Security's default header writer so the
                // vulnerable test app exposes missing security headers to the scanner.
                .headers(headers -> headers.disable())
                .oauth2Login(Customizer.withDefaults()); // login with Keycloak

        return http.build();
    }
}
