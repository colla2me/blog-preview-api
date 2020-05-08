package com.github.service;

import com.github.dao.BlogDao;
import com.github.entity.User;
import com.github.entity.Blog;
import com.github.entity.BlogResult;
import com.github.entity.BlogListResult;
import com.github.entity.Pagination;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

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
            List<Blog> blogs = blogDao.selectBlogsByUserId(userId, pagination.offset(), pagination.getPageSize());
            return BlogListResult.success(blogs, pagination);
        } catch (Exception e) {
            return BlogListResult.failure("系统异常");
        }
    }

    public BlogResult getBlogDetailById(int blogId) {
        try {
            return BlogResult.success(blogDao.selectBlogById(blogId), "获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure("系统异常");
        }
    }

    public BlogResult insertBlog(Blog newBlog) {
        try {
            return BlogResult.success(blogDao.insertBlog(newBlog), "创建成功");
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult updateBlogById(int blogId, Blog blog) {
        Blog myBlog = blogDao.selectBlogById(blogId);
        if (myBlog == null) {
            return BlogResult.failure("博客不存在");
        }

        if (!Objects.equals(blog.getUserId(), myBlog.getUserId())) {
            return BlogResult.failure("无法修改别人的博客");
        }

        try {
            blog.setId(blogId);
            return BlogResult.success(blogDao.updateBlog(blog), "修改成功");
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult deleteBlogById(int blogId, User user) {
        Blog myBlog = blogDao.selectBlogById(blogId);
        if (myBlog == null) {
            return BlogResult.failure("博客不存在");
        }

        if (!Objects.equals(user.getId(), myBlog.getUserId())) {
            return BlogResult.failure("无法删除别人的博客");
        }

        try {
            blogDao.deleteBlogById(blogId);
            return BlogResult.success("删除成功");
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }
}
