package com.capstone.security;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final KeycloakLogoutSuccessHandler keycloakLogoutSuccessHandler;

    public SecurityConfig(KeycloakLogoutSuccessHandler keycloakLogoutSuccessHandler) {
        this.keycloakLogoutSuccessHandler = keycloakLogoutSuccessHandler;
    }

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
                        // Browser login entry and OAuth2 authorization-code callbacks.
                        .requestMatchers(
                                "/login",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()
                        // Scanner JSON login and session probe (401 JSON, not OAuth redirect).
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/session/me"
                        ).permitAll()
                        // Keep /error anonymous so forwarded 500 bodies from injectable
                        // endpoints remain visible instead of an OAuth redirect.
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                // Intentionally disable Spring Security's default header writer so the
                // vulnerable test app exposes missing security headers to the scanner.
                .headers(headers -> headers.disable())
                .oauth2Login(oauth2 -> oauth2.loginPage("/login"))
                .logout(logout -> logout.logoutSuccessHandler(keycloakLogoutSuccessHandler));

        return http.build();
    }
}
