package com.github.service;

import com.github.dao.BlogDao;
import com.github.entity.Blog;
import com.github.entity.BlogListResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.refEq;
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
    void loadBlogsFromDatabase() {
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
    void failedWhenExceptionThrown() {
        when(blogDao.selectBlogsByUserId(any(), anyInt(), anyInt())).thenThrow(new RuntimeException());
        BlogListResult result = blogService.getBlogsByUserId(null, 1, 2);
        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("系统异常", result.getMsg());
    }
}
