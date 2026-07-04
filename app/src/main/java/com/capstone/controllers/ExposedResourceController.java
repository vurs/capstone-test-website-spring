package com.capstone.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ExposedResourceController {

    @GetMapping(value = {"/admin", "/admin/"}, produces = MediaType.TEXT_HTML_VALUE)
    public String adminPanel() {
        return """
                <html>
                    <head><title>Admin Console</title></head>
                    <body>
                        <h1>Admin Console</h1>
                        <p>Intentionally exposed admin page for scanner testing.</p>
                    </body>
                </html>
                """;
    }

    @GetMapping(value = {"/backup", "/backup/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> backupDirectory() {
        return Map.of(
                "directory", "/backup",
                "description", "Intentionally exposed backup listing for scanner testing.",
                "files", List.of("db_backup.sql", "site.bak", "backup.zip")
        );
    }

    @GetMapping(value = {"/uploads", "/uploads/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> uploadsDirectory() {
        return Map.of(
                "directory", "/uploads",
                "description", "Intentionally exposed upload listing for scanner testing.",
                "files", List.of("avatar-test.png", "resume-test.pdf", "debug-dump.txt")
        );
    }

    @GetMapping(value = {"/config", "/config/"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String configDirectory() {
        return """
                # Intentionally exposed config directory index
                application.yml
                config.yml
                .env
                masking-samples.txt
                """;
    }

    @GetMapping(value = "/debug-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> debugInfo() {
        return Map.of(
                "application", "capstone-test-website-spring",
                "environment", "test",
                "debug", true,
                "database", "jdbc:postgresql://postgres:5432/capstone",
                "sampleCredentials", Map.of(
                        "password", "debug-password-for-mask-test",
                        "token", "ghp_debugTokenForMasking1234567890abcdef",
                        "session_id", "sess_debugMaskingSample1234567890",
                        "api_key", "sk_test_debugMaskingSample1234567890"
                ),
                "capturedHeaders", List.of(
                        "Cookie: JSESSIONID=debug-session-cookie; session_id=sess_cookieMaskingSample123",
                        "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.masking.sample"
                )
        );
    }

    @GetMapping(value = "/.env", produces = MediaType.TEXT_PLAIN_VALUE)
    public String envFile() {
        return """
                SPRING_PROFILES_ACTIVE=docker
                POSTGRES_USER=capstone_user
                POSTGRES_PASSWORD=capstone_password
                KEYCLOAK_CLIENT_SECRET=test-client-secret
                password=env-password-for-mask-test
                token=ghp_envMaskingSample1234567890abcdef
                session_id=sess_envMaskingSample1234567890
                api_key=sk_test_envMaskingSample1234567890
                Cookie: tracking_id=masking-demo; session_id=sess_cookieMaskingSample456
                Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.env.masking
                """;
    }

    @GetMapping(value = "/config.yml", produces = MediaType.TEXT_PLAIN_VALUE)
    public String configFile() {
        return """
                spring:
                  datasource:
                    url: jdbc:postgresql://postgres:5432/capstone
                    username: capstone_user
                    password: capstone_password
                  integrations:
                    github_token: ghp_configMaskingSample1234567890abcdef
                    api_key: sk_test_configMaskingSample1234567890
                    session_id: sess_configMaskingSample1234567890
                  outbound_headers:
                    cookie: "Cookie: preview_id=masking-demo; session_id=sess_configCookieMaskingSample"
                    authorization: "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.config.masking"
                """;
    }

    @GetMapping(value = {"/masking-samples", "/masking-samples.txt"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String maskingSamples() {
        return """
                # Intentionally exposed fake secrets for report-layer masking tests
                password=correct-horse-mask-test
                token=ghp_maskingSample1234567890abcdefABCDEF
                session_id=sess_maskingSample1234567890abcdef
                api_key=sk_test_maskingSample1234567890abcdef
                Cookie: JSESSIONID=masking-demo-session; session_id=sess_cookieMaskingSample789
                Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.masking.sample

                # Common standalone key-looking values
                AKIAIOSFODNN7EXAMPLE
                AIzaSyA1b2C3d4E5f6G7h8I9j0K1l2M3n4O5p6Q
                xoxb-mask-test-token-redacted-before-push-protection
                """;
    }

    @GetMapping(value = {"/backup.zip", "/backup.tar.gz", "/db_backup.sql", "/database.sql", "/site.bak", "/app.bak"},
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String backupFile() {
        return """
                Intentionally exposed backup artifact for scanner testing.
                Contains representative sensitive backup content only.
                """;
    }

    @GetMapping(value = "/.git/config", produces = MediaType.TEXT_PLAIN_VALUE)
    public String gitConfig() {
        return """
                [remote "origin"]
                    url = https://github.com/vurs/capstone-test-website-spring.git
                """;
    }
}
