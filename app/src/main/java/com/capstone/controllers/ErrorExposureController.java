package com.capstone.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorExposureController {

    @GetMapping(value = "/error/python-stack", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> pythonStack() {
        return ResponseEntity.internalServerError().body("""
                Traceback (most recent call last):
                  File "/app/main.py", line 42, in index
                    return render_user_profile(user_id)
                RuntimeError: failed to render profile for user 1001
                """);
    }

    @GetMapping(value = "/error/java-stack", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> javaStack() {
        return ResponseEntity.internalServerError().body("""
                java.lang.IllegalStateException: Unable to load user account
                    at com.example.demo.UserController.getUser(UserController.java:27)
                    at com.example.demo.UserService.findById(UserService.java:41)
                    at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)
                """);
    }

    @GetMapping(value = "/error/node-stack", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> nodeStack() {
        return ResponseEntity.internalServerError().body("""
                TypeError: Cannot read properties of undefined (reading 'email')
                    at getUser (/app/server.js:14:9)
                    at Layer.handleRequest (/app/node_modules/express/lib/router/layer.js:95:5)
                """);
    }

    @GetMapping(value = "/error/php-error", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> phpError() {
        return ResponseEntity.internalServerError().body("""
                <html>
                    <head><title>PHP Fatal Error</title></head>
                    <body>
                        <b>Fatal error</b>: Uncaught Exception in /var/www/html/index.php on line 12
                        <pre>PDOException: SQLSTATE[42000]: Syntax error or access violation</pre>
                        <p>Illuminate\\Database\\QueryException</p>
                        <p>Whoops, looks like something went wrong</p>
                    </body>
                </html>
                """);
    }

    @GetMapping(value = "/error/django-debug", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> djangoDebug() {
        return ResponseEntity.ok("""
                <html>
                    <head><title>Django Debug</title></head>
                    <body>
                        <h1>Server Error (500)</h1>
                        <p>Exception Type: ValueError</p>
                        <p>Exception Value: invalid literal for int() with base 10: 'demo'</p>
                        <p>DEBUG = True</p>
                        <pre>Traceback (most recent call last):
                  File "/app/main.py", line 42, in index
                    raise ValueError("demo")</pre>
                    </body>
                </html>
                """);
    }

    @GetMapping(value = "/error/spring-whitelabel", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> springWhitelabel() {
        return ResponseEntity.internalServerError().body("""
                <html>
                    <head><title>Whitelabel Error Page</title></head>
                    <body>
                        <h1>Whitelabel Error Page</h1>
                        <p>There was an unexpected error (type=Internal Server Error)</p>
                        <pre>org.springframework.web.servlet.NoHandlerFoundException: No endpoint GET /users/42</pre>
                    </body>
                </html>
                """);
    }

    @GetMapping(value = "/error/database-error", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> databaseError() {
        return ResponseEntity.internalServerError().body("""
                SQL syntax error near 'FROM users WHERE id ='
                You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version
                SQLSTATE[42601]: Syntax error: 7 ERROR: postgresql query failed
                PDOException: sqlite error: no such table: users
                ORA-01756: quoted string not properly terminated
                Microsoft OLE DB Provider for SQL Server error '80040e14'
                ActionController::StatementInvalid
                ActiveRecord::StatementInvalid
                Rails.root: /srv/app/current
                """);
    }

    @GetMapping(value = "/api/error/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiErrorUsers() {
        return ResponseEntity.internalServerError().body("""
                {
                  "timestamp": "2026-06-15T22:30:00.000Z",
                  "status": 500,
                  "error": "Internal Server Error",
                  "message": "SQLSTATE[42601]: syntax error at or near \\"FROM\\"",
                  "trace": "at com.example.demo.UserController.getUser(UserController.java:27)\\n at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)",
                  "path": "/api/error/users"
                }
                """);
    }
}
