package com.capstone.config;

import com.capstone.models.AppUser;
import com.capstone.repositories.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VulnDataLoader implements CommandLineRunner {

    private final AppUserRepository appUserRepository;

    public VulnDataLoader(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void run(String... args) {
        if (appUserRepository.count() > 0) {
            return;
        }

        AppUser alice = new AppUser();
        alice.setUsername("alice");
        alice.setEmail("alice@example.com");

        AppUser bob = new AppUser();
        bob.setUsername("bob");
        bob.setEmail("bob@example.com");

        AppUser carol = new AppUser();
        carol.setUsername("carol");
        carol.setEmail("carol@example.com");

        appUserRepository.saveAll(List.of(alice, bob, carol));
    }
}
