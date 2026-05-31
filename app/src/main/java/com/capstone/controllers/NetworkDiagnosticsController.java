package com.capstone.controllers;

import com.capstone.services.NetworkDiagnosticsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/network")
public class NetworkDiagnosticsController {

    private final NetworkDiagnosticsService networkDiagnosticsService;

    public NetworkDiagnosticsController(NetworkDiagnosticsService networkDiagnosticsService) {
        this.networkDiagnosticsService = networkDiagnosticsService;
    }

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping(@RequestParam String host) {
        try {
            return ResponseEntity.ok(networkDiagnosticsService.pingHost(host));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().body("Ping interrupted");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
