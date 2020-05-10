package com.github.controller;

import com.github.entity.User;
import com.github.service.AuthService;
import com.github.service.BlogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class BlogControllerTest {

    private MockMvc mvc;

    @Mock
    AuthService authService;

    @Mock
    BlogService blogService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new BlogController(authService, blogService)).build();
    }

    @Test
    public void testRequireLoginBeforeNewBlog() throws Exception {
        mvc.perform(post("/blog")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录后才能操作")));
    }

    @Test
    public void testRequireLoginBeforeUpdatingBlog() throws Exception {
        mvc.perform(patch("/blog/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录后才能操作")));
    }

    @Test
    public void testRequireLoginBeforeDeletingBlog() throws Exception {
        mvc.perform(delete("/blog/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录后才能操作")));
    }

    @Test
    public void testInvalidIfBlogTitleIsEmpty() throws Exception {
        when(authService.getCurrentUser())
                .thenReturn(Optional.of(new User(1, "mockUser", "")));
        mvc.perform(post("/blog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"content\": \"blog body\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("title is invalid")));
    }

    @Test
    public void testInvalidIfBlogContentIsEmpty() throws Exception {
        when(authService.getCurrentUser())
                .thenReturn(Optional.of(new User(1, "mockUser", "")));
        mvc.perform(post("/blog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"title\": \"blog title\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("content is invalid")));
    }

}