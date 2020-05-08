package com.github.controller;

import com.github.entity.AuthResult;
import com.github.entity.User;
import com.github.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // 长度1到15个字符，只能是中文字母数字下划线
    private static final Pattern USER_REGEX = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$");
    // 长度6到16个任意字符
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^.{6,16}$");

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public AuthResult auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        User user = userService.getUserByUsername(username);
        return AuthResult.success(user, user != null ? "用户已登录" : "用户未登录");
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public AuthResult login(@RequestBody Map<String, Object> params) {
        System.out.println("login params: " + params);
        String username = params.get("username").toString();
        String password = params.get("password").toString();

        try {
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password);
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return AuthResult.success(userService.getUserByUsername(username), "登录成功");
        } catch (UsernameNotFoundException e) {
            return AuthResult.failure("用户不存在");
        } catch (BadCredentialsException e) {
            return AuthResult.failure("密码不正确");
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public AuthResult register(@RequestBody Map<String, String> params) {
        System.out.println("register params: " + params);
        String username = params.get("username");
        String password = params.get("password");

        if (!USER_REGEX.matcher(username).matches()) {
            return AuthResult.failure("the username is invalid");
        }

        if (!PASSWORD_REGEX.matcher(password).matches()) {
            return AuthResult.failure("the password is invalid");
        }

        try {
            userService.save(username, password);
            return AuthResult.success(userService.getUserByUsername(username), "注册成功");
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return AuthResult.failure("用户名已存在");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public AuthResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return AuthResult.failure("用户尚未登录");
        }

        SecurityContextHolder.clearContext();
        return AuthResult.success("注销成功");
    }
}
