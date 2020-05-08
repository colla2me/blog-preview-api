package com.github.entity;

public class AuthResult extends Result<User> {

    private boolean isLogin;
    private String avatar;

    protected AuthResult(ResultStatus status, String msg, User user) {
        super(status, msg, user);
        this.isLogin = user != null;
        this.avatar = isLogin ? user.getAvatar() : "";
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getAvatar() {
        return avatar;
    }

    public static AuthResult failure(String msg) {
        return new AuthResult(ResultStatus.FAIL, msg, null);
    }

    public static AuthResult success(String msg) {
        return success(null, msg);
    }

    public static AuthResult success(User user, String msg) {
        return new AuthResult(ResultStatus.OK, msg, user);
    }
}