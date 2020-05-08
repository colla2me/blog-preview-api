package com.github.service;

import com.github.dao.BlogDao;
import com.github.entity.BlogListResult;
import com.github.entity.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        blogService.getBlogsByUserId(null, 1, 10);
        verify(blogDao).loadBlogsByUserId(null, Pagination.of(1, 10));
    }

    @Test
    void loadFailedWhenThrowException() {
        when(blogDao.loadBlogsByUserId(null, Pagination.of(1, 10))).thenThrow(new RuntimeException());
        BlogListResult result = blogService.getBlogsByUserId(null, 1, 10);
        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("系统异常", result.getMsg());
    }
}
