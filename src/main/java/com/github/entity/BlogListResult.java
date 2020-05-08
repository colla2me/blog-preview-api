package com.github.entity;

import java.util.List;

public class BlogListResult extends Result<List<Blog>> {
    private int total;
    private int page;
    private int totalPage;

    private BlogListResult(ResultStatus status, String msg, List<Blog> blogs, Pagination pagination) {
        super(status, msg, blogs);
        this.total = pagination.getTotal();
        this.page = pagination.getPage();
        this.totalPage = pagination.getTotalPage();
    }

    public static BlogListResult success(List<Blog> data, Pagination pagination) {
        return new BlogListResult(ResultStatus.OK, "获取成功", data, pagination);
    }

    public static BlogListResult failure(String msg) {
        return new BlogListResult(ResultStatus.FAIL, msg, null, Pagination.of(0, 0));
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
