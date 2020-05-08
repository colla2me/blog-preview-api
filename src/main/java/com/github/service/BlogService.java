package com.github.service;

import com.github.dao.BlogDao;
import com.github.entity.Blog;
import com.github.entity.BlogListResult;
import com.github.entity.Pagination;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {

    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public BlogListResult getBlogsByUserId(Integer userId, int page, int pageSize) {
        try {
            Pagination pagination = Pagination.of(page, pageSize, blogDao.count(userId));
            List<Blog> blogs = blogDao.loadBlogsByUserId(userId, pagination);
            return BlogListResult.success(blogs, pagination);
        } catch (Exception e) {
            return BlogListResult.failure("系统异常");
        }
    }
}
