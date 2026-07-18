package com.capstone.controllers;

import com.capstone.models.BlogPost;
import com.capstone.services.BlogPostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class BlogController {

    private final BlogPostService blogPostService;

    public BlogController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        model.addAttribute("isAuthenticated", principal != null);
        return "index";
    }

    @GetMapping("/post-list")
    public String viewPostList(Model model) {
        model.addAttribute("posts", blogPostService.getAllPosts());
        return "post-list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("post", new BlogPost());
        return "create";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute("post") BlogPost blogPost) {
        blogPostService.save(blogPost);
        return "redirect:/post-list";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", blogPostService.getById(id));
        return "post";
    }
}
