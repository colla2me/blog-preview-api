package com.github.service;

import com.github.dao.BlogDao;
import com.github.entity.Blog;
import com.github.entity.BlogListResult;
import com.github.entity.BlogResult;
import com.github.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogDao blogDao;

    @InjectMocks
    BlogService blogService;

    @Test
    public void testGetBlogsFromDatabase() {
        List<Blog> blogs = Arrays.asList(mock(Blog.class), mock(Blog.class));
        when(blogDao.selectBlogsByUserId(null, 0, 2)).thenReturn(blogs);
        when(blogDao.count(null)).thenReturn(3);

        BlogListResult result = blogService.getBlogsByUserId(null, 1, 2);
        verify(blogDao).count(null);
        verify(blogDao).selectBlogsByUserId(null, 0, 2);

        Assertions.assertEquals(1, result.getPage());
        Assertions.assertEquals(3, result.getTotal());
        Assertions.assertEquals(2, result.getTotalPage());
        Assertions.assertEquals("ok", result.getStatus());
    }

    @Test
    public void throwExceptionWhenRequestFailed() {
        when(blogDao.selectBlogsByUserId(any(), anyInt(), anyInt())).thenThrow(new RuntimeException());
        BlogListResult result = blogService.getBlogsByUserId(null, 1, 2);
        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("系统异常", result.getMsg());
    }

    @Test
    public void testInsertANewBlog() {
        Blog newBlog = new Blog();
        newBlog.setId(1);

        when(blogDao.insertBlog(newBlog)).thenReturn(newBlog);
        BlogResult result = blogService.insertBlog(newBlog);
        Assertions.assertEquals("ok", result.getStatus());
    }

    @Test
    public void testDeletionWhenBlogNotFound() {
        when(blogDao.selectBlogById(1)).thenReturn(null);
        BlogResult result = blogService.deleteBlogById(1, mock(User.class));
        Assertions.assertEquals("fail", result.getStatus());
    }

    @Test
    public void testDeletionWhenBlogUserIdNotMatch() {
        User author = new User(123, "author", "");
        User targetUser = new User(456, "user", "");

        Blog blogInDb = new Blog();
        blogInDb.setUser(author);

        when(blogDao.selectBlogById(1)).thenReturn(blogInDb);
        BlogResult result = blogService.deleteBlogById(1, targetUser);

        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("无法删除别人的博客", result.getMsg());
    }

    @Test
    public void testUpdateWhenBlogNotFound() {
        when(blogDao.selectBlogById(1)).thenReturn(null);
        BlogResult result = blogService.updateBlogById(1, mock(Blog.class));

        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("博客不存在", result.getMsg());
    }

    @Test
    public void testUpdateWhenBlogUserIdNotMatch() {
        User blogAuthor = new User(123, "author", "");
        User targetUser = new User(456, "user", "");

        Blog targetBlog = new Blog();
        targetBlog.setId(1);
        targetBlog.setUser(targetUser);

        Blog blogInDb = new Blog();
        blogInDb.setUser(blogAuthor);

        when(blogDao.selectBlogById(1)).thenReturn(blogInDb);
        BlogResult result = blogService.updateBlogById(1, targetBlog);

        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("无法修改别人的博客", result.getMsg());
    }
}
