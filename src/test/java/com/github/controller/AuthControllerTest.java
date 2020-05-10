package com.github.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc;

    static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    UserService userService;
    @Mock
    AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new AuthController(userService, authenticationManager))
                .build();
    }

    @Test
    public void testAuthWhenUserNotLogin() throws Exception {
        mvc.perform(get("/auth"))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("未登录")));
    }

    @Test
    public void testAuthWhenUserLoggedIn() throws Exception {
        mvc.perform(get("/auth"))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("未登录")));

        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "password");
        String json = new ObjectMapper().writeValueAsString(params);

        when(userService.loadUserByUsername("user"))
                .thenReturn(new User("user", "password", Collections.emptyList()));

        when(userService.getUserByUsername("user"))
                .thenReturn(new com.github.entity.User(1, "user", passwordEncoder.encode("password")));

        MvcResult mvcResult = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("登录成功")))
                .andReturn();

        HttpSession session = mvcResult.getRequest().getSession();
        Assertions.assertNotNull(session);
        mvc.perform(get("/auth").session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("已登录")));
    }

    @Test
    public void testRegisterParametersIsInvalid() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "");
        params.put("password", "");
        String json = new ObjectMapper().writeValueAsString(params);

        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("invalid")));
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "password");
        String json = new ObjectMapper().writeValueAsString(params);

        mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("注册成功")));
    }

    @Test
    public void testLogoutIfUserNotLogin() throws Exception {
        mvc.perform(get("/auth/logout"))
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("尚未登录")));
    }

    @Test
    public void testLogoutIfUserLoggedIn() throws Exception {
        when(userService.getUserByUsername(null))
                .thenReturn(new com.github.entity.User(1, "user", passwordEncoder.encode("password")));

        mvc.perform(get("/auth/logout"))
                .andDo(result -> result.getResponse().setHeader("Content-type", "application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("注销成功")));
    }
}