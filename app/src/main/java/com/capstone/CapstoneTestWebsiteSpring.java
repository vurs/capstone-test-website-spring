package com.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CapstoneTestWebsiteSpring {

	public static void main(String[] args) {
		try {
			DevEnvironmentGuard.requireSetupComplete();
		} catch (IllegalStateException exception) {
			System.err.println(exception.getMessage());
			System.exit(1);
			return;
		}

		SpringApplication.run(CapstoneTestWebsiteSpring.class, args);
	}

}
