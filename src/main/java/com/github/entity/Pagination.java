package com.github.entity;

public class Pagination {
    private final int page;
    private final int pageSize;
    private final int total;
    private final int totalPage;

    private Pagination(int page, int pageSize, int total) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    public static Pagination of(int page, int total) {
        return of(page, 10, total);
    }

    public static Pagination of(int page, int pageSize, int total) {
        return new Pagination(page, pageSize, total);
    }

    public int getPage() {
        return Math.min(page, totalPage);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public int offset() {
        return (getPage() - 1) * pageSize;
    }
}
