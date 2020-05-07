package com.github.mapper;

import com.github.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USER WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("INSERT INTO USER(username, encrypted_password) values(#{username}, #{password})")
    void save(@Param("username") String username, @Param("password") String password);
}
