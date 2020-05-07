package com.github.entity;

public class AuthResult extends Result {

    private boolean isLogin;

    public static AuthResult withLoggedInUser(User user) {
        boolean isLogin = user != null;
        String msg = isLogin ? "用户已登录" : "用户未登录";
        return new AuthResult("ok", msg, user, isLogin);
    }

    private AuthResult(String status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }
}