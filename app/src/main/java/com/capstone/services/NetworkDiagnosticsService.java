package com.capstone.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class NetworkDiagnosticsService {

    public String pingHost(String host) throws IOException, InterruptedException {
        // Intentionally vulnerable: user input is concatenated into a shell command.
        Process process = Runtime.getRuntime().exec(new String[]{
                "/bin/sh", "-c", "ping -c 1 " + host
        });

        String stdout = readStream(process.getInputStream());
        String stderr = readStream(process.getErrorStream());
        int exitCode = process.waitFor();

        StringBuilder output = new StringBuilder();
        if (!stdout.isBlank()) {
            output.append(stdout.trim());
        }
        if (!stderr.isBlank()) {
            if (!output.isEmpty()) {
                output.append("\n");
            }
            output.append(stderr.trim());
        }
        if (output.isEmpty()) {
            output.append("(no output, exit code ").append(exitCode).append(")");
        }
        return output.toString();
    }

    private static String readStream(InputStream stream) throws IOException {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
