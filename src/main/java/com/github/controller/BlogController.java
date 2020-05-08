package com.github.controller;

import com.github.entity.BlogListResult;
import com.github.service.BlogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class BlogController {
    private final BlogService blogService;

    @Inject
    public BlogController(BlogService blogService) {
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
}
