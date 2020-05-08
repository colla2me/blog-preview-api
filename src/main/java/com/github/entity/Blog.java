package com.github.entity;

import com.github.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Map;

public class Blog {
    private Integer id;
    private User user;
    private String title;
    private String content;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() { return user; }

    public Integer getUserId() {
        return user != null ? user.getId() : null;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public static Blog fromParam(Map<String, String> params, User user) {
        Blog blog = new Blog();
        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");

        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "title is invalid!");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 10000, "content is invalid");

        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }

        blog.title = title;
        blog.content = content;
        blog.description = description;
        blog.user = user;
        return blog;
    }
}
