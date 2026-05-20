package com.capstone.security;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/users", "/users/search").permitAll()
                        .anyRequest().authenticated()
                )
                // Intentionally disable Spring Security's default header writer so the
                // vulnerable test app exposes missing security headers to the scanner.
                .headers(headers -> headers.disable())
                .oauth2Login(Customizer.withDefaults()); // login with Keycloak

        return http.build();
    }
}
