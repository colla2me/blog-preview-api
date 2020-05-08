package com.github.dao;

import com.github.entity.Blog;
import com.github.entity.Pagination;
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

    private Map<String, Object> asMap(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            result.put(args[i].toString(), args[i + 1]);
        }
        return result;
    }

    public List<Blog> loadBlogsByUserId(Integer userId, Pagination pagination) {
        Map<String, Object> parameters = asMap(
                "user_id", userId,
                "offset", pagination.offset(),
                "limit", pagination.limit());
        return sqlSession.selectList("selectBlogs", parameters);
    }

    public int count(Integer userId) {
        return sqlSession.selectOne("countBlog", asMap("userId", userId));
    }
}
