package com.github.dao;

import com.github.entity.Blog;
import com.github.entity.BlogResult;
import com.github.entity.Pagination;
import com.github.utils.MapUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Blog> selectBlogsByUserId(Integer userId, int offset, int limit) {
        Map<String, Object> parameters = MapUtils.asMap(
                "userId", userId,
                "offset", offset,
                "limit", limit);
        return sqlSession.selectList("selectBlogs", parameters);
    }

    public int count(Integer userId) {
        return sqlSession.selectOne("countBlog", MapUtils.asMap("userId", userId));
    }

    public Blog selectBlogById(int blogId) {
        return sqlSession.selectOne("selectBlogById", MapUtils.asMap("id", blogId));
    }

    public Blog insertBlog(Blog newBlog) {
        sqlSession.insert("insertBlog", newBlog);
        return selectBlogById(newBlog.getId());
    }

    public Blog updateBlog(Blog myBlog) {
        sqlSession.update("updateBlog", myBlog);
        return selectBlogById(myBlog.getId());
    }

    public void deleteBlogById(int blogId) {
        sqlSession.delete("deleteBlog", blogId);
    }
}
