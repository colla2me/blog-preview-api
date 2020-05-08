package com.github.entity;

public class BlogResult extends Result<Blog> {

    private BlogResult(ResultStatus status, String msg, Blog data) {
        super(status, msg, data);
    }

    public static BlogResult success(String msg) {
        return success(null, msg);
    }

    public static BlogResult success(Blog blog, String msg) {
        return new BlogResult(ResultStatus.OK, msg, blog);
    }

    public static BlogResult failure(Exception e) {
        return failure(e.getMessage());
    }

    public static BlogResult failure(String msg) {
        return new BlogResult(ResultStatus.FAIL, msg, null);
    }
}
