package com.github.controller;

import com.github.entity.Blog;
import com.github.entity.BlogListResult;
import com.github.entity.BlogResult;
import com.github.service.AuthService;
import com.github.service.BlogService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class BlogController {
    private final AuthService authService;
    private final BlogService blogService;

    @Inject
    public BlogController(AuthService authService, BlogService blogService) {
        this.authService = authService;
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public BlogListResult getBlogs(
            @RequestParam("page") Integer page,
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "atIndex", required = false) Boolean atIndex) {
        if (page == null || page == 0) {
            page = 1;
        }

        return blogService.getBlogsByUserId(userId, page, 10);
    }

    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult getBlogDetail(@PathVariable("blogId") int blogId) {
        return blogService.getBlogDetailById(blogId);
    }

    @PostMapping("/blog")
    @ResponseBody
    public BlogResult createBlog(@RequestBody Map<String, String> params) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.insertBlog(Blog.fromParam(params, user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e);
        }
    }

    @PatchMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult editBlog(@PathVariable("blogId") int blogId, @RequestBody Map<String, String> param) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlogById(blogId, Blog.fromParam(param, user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e);
        }
    }

    @DeleteMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult deleteBlog(@PathVariable("blogId") int blogId) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.deleteBlogById(blogId, user))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e);
        }
    }
}
