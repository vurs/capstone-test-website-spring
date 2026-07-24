package com.capstone.config;

import com.capstone.models.AppUser;
import com.capstone.repositories.AppUserRepository;
import com.capstone.repositories.BlogPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VulnDataLoader implements CommandLineRunner {

    private static final int MAX_BLOG_POSTS = 50;

    private final AppUserRepository appUserRepository;
    private final BlogPostRepository blogPostRepository;

    public VulnDataLoader(AppUserRepository appUserRepository, BlogPostRepository blogPostRepository) {
        this.appUserRepository = appUserRepository;
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public void run(String... args) {
        seedUsersIfNeeded();
        pruneBlogPosts();
    }

    private void seedUsersIfNeeded() {
        if (appUserRepository.count() == 0) {
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

    private void pruneBlogPosts() {
        if (blogPostRepository.count() > MAX_BLOG_POSTS) {
            blogPostRepository.deleteOlderPostsKeepingNewest(MAX_BLOG_POSTS);
        }
    }
}
